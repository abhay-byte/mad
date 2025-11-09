import 'dart:async';
import 'package:mqtt_client/mqtt_client.dart';
import 'package:mqtt_client/mqtt_server_client.dart';

enum MQTTConnectionState {
  disconnected,
  connecting,
  connected,
  error,
}

class MQTTService {
  MqttServerClient? _client;
  String? _host;
  String? _identifier;
  String? _username;
  String? _password;
  final _connectionStateController =
      StreamController<MQTTConnectionState>.broadcast();
  final _messageController = StreamController<String>.broadcast();
  final _publishController = StreamController<MqttPublishMessage>.broadcast();

  // Track connection metrics
  int _pongCount = 0;
  int _pingCount = 0;

  MQTTService() {
    _connectionStateController.add(MQTTConnectionState.disconnected);
  }

  Stream<MQTTConnectionState> get connectionState =>
      _connectionStateController.stream;
  Stream<String> get messageStream => _messageController.stream;
  Stream<MqttPublishMessage> get publishStream => _publishController.stream;

  int get pongCount => _pongCount;
  int get pingCount => _pingCount;
  int? get lastPingPongLatency => _client?.lastCycleLatency;
  int? get averagePingPongLatency => _client?.averageCycleLatency;

  Future<void> initialize({
    required String host,
    String identifier = '',
    String? username,
    String? password,
    bool useWebSocket = false,
    int? port,
    int keepAlivePeriod = 20,
    int connectTimeoutPeriod = 2000,
  }) async {
    _host = host;
    _identifier = identifier.isEmpty
        ? 'mqtt_${DateTime.now().millisecondsSinceEpoch}'
        : identifier;
    _username = username;
    _password = password;

    _connectionStateController.add(MQTTConnectionState.connecting);

    try {
      // Create a new client for each initialization
      _client = MqttServerClient(
        host,
        _identifier!,
        maxConnectionAttempts: 3,
      );

      // Configure client settings
      if (useWebSocket) {
        _client!.useWebSocket = true;
        _client!.port = port ?? 80;
      } else {
        _client!.port = port ?? 1883;
      }

      await _setupMqttClient(
        keepAlivePeriod: keepAlivePeriod,
        connectTimeoutPeriod: connectTimeoutPeriod,
      );
      _connectionStateController.add(MQTTConnectionState.connected);
    } catch (e) {
      print('MQTT Initialization Error: $e');
      _connectionStateController.add(MQTTConnectionState.error);
      rethrow;
    }
  }

  Future<void> _setupMqttClient({
    required int keepAlivePeriod,
    required int connectTimeoutPeriod,
  }) async {
    if (_client == null) return;

    // Configure client properties
    _client!.logging(on: false);
    _client!.keepAlivePeriod = keepAlivePeriod;
    _client!.connectTimeoutPeriod = connectTimeoutPeriod;

    // Set protocol version to 3.1.1
    _client!.setProtocolV311();

    // Configure callbacks
    _client!.onConnected = _onConnected;
    _client!.onDisconnected = _onDisconnected;
    _client!.onSubscribed = _onSubscribed;
    _client!.onUnsubscribed = _onUnsubscribed;
    _client!.pongCallback = _pong;
    _client!.pingCallback = _ping;

    // Configure connection message
    final connMessage = MqttConnectMessage()
        .withClientIdentifier(_identifier!)
        .startClean() // Non persistent session
        .withWillQos(MqttQos.atLeastOnce);

    if (_username != null && _password != null) {
      connMessage.authenticateAs(_username!, _password!);
    }

    _client!.connectionMessage = connMessage;

    // Set up message handling
    _client!.published!.listen(_onPublished);
    _client!.updates!.listen(_onMessage);

    try {
      await _client!.connect();
    } catch (e) {
      print('MQTT Connection Error: $e');
      _connectionStateController.add(MQTTConnectionState.error);
      _client!.disconnect();
      rethrow;
    }
  }

  Future<void> subscribe(String topic,
      {MqttQos qos = MqttQos.atLeastOnce}) async {
    if (_client?.connectionStatus?.state != MqttConnectionState.connected) {
      throw Exception('MQTT client is not connected');
    }

    try {
      print('Subscribing to topic: $topic with QoS $qos');
      _client!.subscribe(topic, qos);
    } catch (e) {
      print('MQTT Subscribe Error: $e');
      rethrow;
    }
  }

  Future<void> unsubscribe(String topic) async {
    if (_client?.connectionStatus?.state != MqttConnectionState.connected) {
      throw Exception('MQTT client is not connected');
    }

    try {
      print('Unsubscribing from topic: $topic');
      _client!.unsubscribe(topic);
    } catch (e) {
      print('MQTT Unsubscribe Error: $e');
      rethrow;
    }
  }

  Future<void> publishMessage(
    String topic,
    String message, {
    MqttQos qos = MqttQos.atLeastOnce,
    bool retain = false,
  }) async {
    if (_client?.connectionStatus?.state != MqttConnectionState.connected) {
      throw Exception('MQTT client is not connected');
    }

    try {
      final builder = MqttClientPayloadBuilder();
      builder.addString(message);

      print('Publishing to topic: $topic with QoS $qos');
      _client!.publishMessage(topic, qos, builder.payload!, retain: retain);
    } catch (e) {
      print('MQTT Publish Error: $e');
      rethrow;
    }
  }

  void _onConnected() {
    print('MQTT Client Connected');
    _connectionStateController.add(MQTTConnectionState.connected);
  }

  void _onDisconnected() {
    print('MQTT Client Disconnected');
    _connectionStateController.add(MQTTConnectionState.disconnected);

    if (_client?.connectionStatus?.disconnectionOrigin ==
        MqttDisconnectionOrigin.unsolicited) {
      print('Warning: Unsolicited disconnection from broker');
    }
  }

  void _onSubscribed(String topic) {
    print('Subscription confirmed for topic $topic');
  }

  void _onUnsubscribed(String? topic) {
    print('Unsubscribed from topic: $topic');
  }

  void _pong() {
    _pongCount++;
    print('Ping response received - Latency: ${_client?.lastCycleLatency}ms');
  }

  void _ping() {
    _pingCount++;
    print('Ping request sent');
  }

  void _onPublished(MqttPublishMessage message) {
    final topic = message.variableHeader!.topicName;
    final qos = message.header!.qos;
    print('Published to $topic with QoS $qos');
    _publishController.add(message);
  }

  void _onMessage(List<MqttReceivedMessage<MqttMessage?>>? messages) {
    if (messages == null || messages.isEmpty) return;

    final recMess = messages[0].payload as MqttPublishMessage;
    final topic = messages[0].topic;

    final payload =
        MqttPublishPayload.bytesToStringAsString(recMess.payload.message);
    print('Message received on topic $topic: $payload');
    _messageController.add(payload);
  }

  Future<void> disconnect() async {
    print('Disconnecting MQTT Client...');
    _client?.disconnect();

    // Close all stream controllers
    await _messageController.close();
    await _connectionStateController.close();
    await _publishController.close();

    print('MQTT Client Disconnected and streams closed');
  }
}
