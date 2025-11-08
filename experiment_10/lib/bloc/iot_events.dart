part of 'iot_bloc.dart';

abstract class IoTEvent extends Equatable {
  const IoTEvent();

  @override
  List<Object> get props => [];
}

class InitializeMQTT extends IoTEvent {
  final String host;
  final String? username;
  final String? password;
  final String identifier;

  const InitializeMQTT({
    required this.host,
    this.username,
    this.password,
    required this.identifier,
  });

  @override
  List<Object> get props => [host, identifier];
}

class IoTDeviceConnected extends IoTEvent {
  final IoTDevice device;

  const IoTDeviceConnected(this.device);

  @override
  List<Object> get props => [device];
}

class IoTDeviceDisconnected extends IoTEvent {
  final String deviceId;

  const IoTDeviceDisconnected(this.deviceId);

  @override
  List<Object> get props => [deviceId];
}

class IoTDeviceStateChanged extends IoTEvent {
  final String deviceId;
  final Map<String, dynamic> newState;

  const IoTDeviceStateChanged(this.deviceId, this.newState);

  @override
  List<Object> get props => [deviceId, newState];
}

class UpdateMQTTState extends IoTEvent {
  final MQTTConnectionState connectionState;

  const UpdateMQTTState(this.connectionState);

  @override
  List<Object> get props => [connectionState];
}
