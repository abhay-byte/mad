import 'package:equatable/equatable.dart';

enum DeviceType { light, thermostat, fan }

enum DeviceStatus { online, offline }

class IoTDevice extends Equatable {
  final String id;
  final String name;
  final DeviceType type;
  final DeviceStatus status;
  final Map<String, dynamic> state;

  const IoTDevice({
    required this.id,
    required this.name,
    required this.type,
    this.status = DeviceStatus.offline,
    this.state = const {},
  });

  IoTDevice copyWith({
    String? id,
    String? name,
    DeviceType? type,
    DeviceStatus? status,
    Map<String, dynamic>? state,
  }) {
    return IoTDevice(
      id: id ?? this.id,
      name: name ?? this.name,
      type: type ?? this.type,
      status: status ?? this.status,
      state: state ?? this.state,
    );
  }

  @override
  List<Object?> get props => [id, name, type, status, state];

  factory IoTDevice.fromJson(Map<String, dynamic> json) {
    return IoTDevice(
      id: json['id'] as String,
      name: json['name'] as String,
      type: DeviceType.values.firstWhere(
        (type) => type.toString() == 'DeviceType.${json['type']}',
      ),
      status: DeviceStatus.values.firstWhere(
        (status) => status.toString() == 'DeviceStatus.${json['status']}',
      ),
      state: json['state'] as Map<String, dynamic>,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'type': type.toString().split('.').last,
      'status': status.toString().split('.').last,
      'state': state,
    };
  }
}
