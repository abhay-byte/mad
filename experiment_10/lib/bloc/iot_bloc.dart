import 'dart:async';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:equatable/equatable.dart';
import '../models/iot_device.dart';
import '../services/mqtt_service.dart';

part 'iot_events.dart';
part 'iot_state.dart';

class IoTBloc extends Bloc<IoTEvent, IoTState> {
  final MQTTService _mqttService;
  StreamSubscription<MQTTConnectionState>? _mqttConnectionSubscription;
  StreamSubscription<String>? _mqttMessageSubscription;

  IoTBloc({required MQTTService mqttService})
      : _mqttService = mqttService,
        super(const IoTState()) {
    on<InitializeMQTT>(_onInitializeMQTT);
    on<UpdateMQTTState>(_onUpdateMQTTState);
    on<IoTDeviceConnected>(_onDeviceConnected);
    on<IoTDeviceDisconnected>(_onDeviceDisconnected);
    on<IoTDeviceStateChanged>(_onDeviceStateChanged);

    // Start listening to MQTT state changes and messages
    _setupSubscriptions();
  }

  void _setupSubscriptions() {
    _mqttConnectionSubscription?.cancel();
    _mqttMessageSubscription?.cancel();

    _mqttConnectionSubscription = _mqttService.connectionState.listen(
      (connectionState) {
        add(UpdateMQTTState(connectionState));
      },
    );

    _mqttMessageSubscription = _mqttService.messageStream.listen(
      (message) {
        // Handle incoming messages, parse and dispatch appropriate events
        try {
          final parsedMessage = message.split(',');
          if (parsedMessage.length >= 2) {
            final deviceId = parsedMessage[0];
            final state =
                Map<String, dynamic>.from({'value': parsedMessage[1]});
            add(IoTDeviceStateChanged(deviceId, state));
          }
        } catch (e) {
          // Handle parse error silently
          print('Error parsing message: $e');
        }
      },
    );
  }

  Future<void> _onInitializeMQTT(
    InitializeMQTT event,
    Emitter<IoTState> emit,
  ) async {
    emit(state.copyWith(isLoading: true, errorMessage: null));

    try {
      await _mqttService.initialize(
        host: event.host,
        identifier: event.identifier,
        username: event.username,
        password: event.password,
      );
      emit(state.copyWith(isInitialized: true));
    } catch (e) {
      emit(state.copyWith(
        isLoading: false,
        errorMessage: 'Failed to initialize MQTT: $e',
      ));
    }
  }

  void _onUpdateMQTTState(
    UpdateMQTTState event,
    Emitter<IoTState> emit,
  ) {
    emit(state.copyWith(
      mqttState: event.connectionState,
      isLoading: false,
      errorMessage: event.connectionState == MQTTConnectionState.error
          ? 'MQTT Connection Error'
          : null,
    ));
  }

  void _onDeviceConnected(
    IoTDeviceConnected event,
    Emitter<IoTState> emit,
  ) {
    final updatedDevices = List<IoTDevice>.from(state.devices)
      ..removeWhere((device) => device.id == event.device.id)
      ..add(event.device);

    emit(state.copyWith(devices: updatedDevices));
  }

  void _onDeviceDisconnected(
    IoTDeviceDisconnected event,
    Emitter<IoTState> emit,
  ) {
    final updatedDevices = state.devices
        .map((device) => device.id == event.deviceId
            ? device.copyWith(status: DeviceStatus.offline)
            : device)
        .toList();

    emit(state.copyWith(devices: updatedDevices));
  }

  void _onDeviceStateChanged(
    IoTDeviceStateChanged event,
    Emitter<IoTState> emit,
  ) {
    try {
      final updatedDevices = state.devices.map((device) {
        if (device.id == event.deviceId) {
          return device.copyWith(state: event.newState);
        }
        return device;
      }).toList();

      emit(state.copyWith(devices: updatedDevices));
    } catch (e) {
      emit(state.copyWith(errorMessage: 'Failed to update device state: $e'));
    }
  }

  @override
  Future<void> close() async {
    await _mqttConnectionSubscription?.cancel();
    await _mqttMessageSubscription
        ?.cancel(); // Also cancel the message subscription
    await _mqttService.disconnect();
    return super.close();
  }
}
