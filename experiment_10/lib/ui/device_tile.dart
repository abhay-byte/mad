import 'package:flutter/material.dart';
import '../models/iot_device.dart';

class DeviceTile extends StatelessWidget {
  final IoTDevice device;
  final Function(Map<String, dynamic>) onStateChanged;

  const DeviceTile({
    super.key,
    required this.device,
    required this.onStateChanged,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        leading: _buildDeviceIcon(),
        title: Text(device.name),
        subtitle: Text('Status: ${device.status.toString().split('.').last}'),
        trailing: _buildDeviceControls(),
      ),
    );
  }

  Widget _buildDeviceIcon() {
    IconData iconData;
    switch (device.type) {
      case DeviceType.light:
        iconData = Icons.lightbulb;
        break;
      case DeviceType.thermostat:
        iconData = Icons.thermostat;
        break;
      case DeviceType.fan:
        iconData = Icons.wind_power;
        break;
    }

    return Icon(
      iconData,
      color: device.status == DeviceStatus.online ? Colors.green : Colors.grey,
    );
  }

  Widget _buildDeviceControls() {
    switch (device.type) {
      case DeviceType.light:
        final isOn = device.state['power'] as bool? ?? false;
        return Switch(
          value: isOn,
          onChanged: (value) {
            onStateChanged({'power': value});
          },
        );

      case DeviceType.thermostat:
        final temperature = device.state['temperature'] as double? ?? 20.0;
        return Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            IconButton(
              icon: const Icon(Icons.remove),
              onPressed: () {
                onStateChanged({'temperature': temperature - 0.5});
              },
            ),
            Text('${temperature.toStringAsFixed(1)}°C'),
            IconButton(
              icon: const Icon(Icons.add),
              onPressed: () {
                onStateChanged({'temperature': temperature + 0.5});
              },
            ),
          ],
        );

      case DeviceType.fan:
        final speed = device.state['speed'] as int? ?? 0;
        return DropdownButton<int>(
          value: speed,
          items: [0, 1, 2, 3].map((value) {
            return DropdownMenuItem<int>(
              value: value,
              child: Text(value == 0 ? 'Off' : 'Speed $value'),
            );
          }).toList(),
          onChanged: (value) {
            if (value != null) {
              onStateChanged({'speed': value});
            }
          },
        );
    }
  }
}
