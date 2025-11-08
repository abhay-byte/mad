part of 'iot_bloc.dart';

// Using MQTTConnectionState from mqtt_service.dart
class IoTState extends Equatable {
  final List<IoTDevice> devices;
  final MQTTConnectionState mqttState;
  final String? errorMessage;
  final bool isInitialized;
  final bool isLoading;

  const IoTState({
    this.devices = const [],
    this.mqttState = MQTTConnectionState.disconnected,
    this.errorMessage,
    this.isInitialized = false,
    this.isLoading = false,
  });

  IoTState copyWith({
    List<IoTDevice>? devices,
    MQTTConnectionState? mqttState,
    String? errorMessage,
    bool? isInitialized,
    bool? isLoading,
  }) {
    return IoTState(
      devices: devices ?? this.devices,
      mqttState: mqttState ?? this.mqttState,
      errorMessage: errorMessage,
      isInitialized: isInitialized ?? this.isInitialized,
      isLoading: isLoading ?? this.isLoading,
    );
  }

  @override
  List<Object?> get props => [devices, mqttState, errorMessage, isInitialized];
}
