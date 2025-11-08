import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import '../bloc/iot_bloc.dart';
import '../services/mqtt_service.dart';
import 'device_tile.dart';

class DeviceListScreen extends StatefulWidget {
  const DeviceListScreen({super.key});

  @override
  State<DeviceListScreen> createState() => _DeviceListScreenState();
}

class _DeviceListScreenState extends State<DeviceListScreen> {
  bool _shouldInitialize = true;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (_shouldInitialize) {
        context.read<IoTBloc>().add(
              InitializeMQTT(
                host: 'test.mosquitto.org', // Using public test broker
                identifier:
                    'flutter_iot_app_${DateTime.now().millisecondsSinceEpoch}',
              ),
            );
        setState(() {
          _shouldInitialize = false;
        });
      }
    });
  }

  Widget _buildConnectionStatus(MQTTConnectionState state) {
    Color color;
    String text;

    switch (state) {
      case MQTTConnectionState.connected:
        color = Colors.green;
        text = 'Connected';
        break;
      case MQTTConnectionState.connecting:
        color = Colors.orange;
        text = 'Connecting...';
        break;
      case MQTTConnectionState.disconnected:
        color = Colors.grey;
        text = 'Disconnected';
        break;
      case MQTTConnectionState.error:
        color = Colors.red;
        text = 'Connection Error';
        break;
    }

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      decoration: BoxDecoration(
        color: color.withOpacity(0.1),
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: color),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(Icons.circle, size: 12, color: color),
          const SizedBox(width: 8),
          Text(text, style: TextStyle(color: color)),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('IoT Device Control'),
        actions: [
          BlocBuilder<IoTBloc, IoTState>(
            builder: (context, state) {
              return Padding(
                padding:
                    const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                child: _buildConnectionStatus(state.mqttState),
              );
            },
          ),
        ],
      ),
      body: BlocBuilder<IoTBloc, IoTState>(
        builder: (context, state) {
          if (state.isLoading) {
            return const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircularProgressIndicator(),
                  SizedBox(height: 16),
                  Text('Connecting to MQTT broker...'),
                ],
              ),
            );
          }

          if (state.errorMessage != null) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Icon(Icons.error_outline, size: 48, color: Colors.red),
                  const SizedBox(height: 16),
                  Text(state.errorMessage!,
                      style: const TextStyle(color: Colors.red)),
                  const SizedBox(height: 16),
                  ElevatedButton(
                    onPressed: () {
                      context.read<IoTBloc>().add(
                            const InitializeMQTT(
                              host: 'test.mosquitto.org',
                              identifier: 'flutter_iot_app_retry',
                            ),
                          );
                    },
                    child: const Text('Retry Connection'),
                  ),
                ],
              ),
            );
          }

          if (state.devices.isEmpty) {
            return const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(Icons.devices_other, size: 48, color: Colors.grey),
                  SizedBox(height: 16),
                  Text(
                    'No devices found',
                    style: TextStyle(fontSize: 16, color: Colors.grey),
                  ),
                ],
              ),
            );
          }

          return ListView.builder(
            itemCount: state.devices.length,
            padding: const EdgeInsets.all(8),
            itemBuilder: (context, index) {
              final device = state.devices[index];
              return DeviceTile(
                device: device,
                onStateChanged: (newState) {
                  context.read<IoTBloc>().add(
                        IoTDeviceStateChanged(device.id, newState),
                      );
                },
              );
            },
          );
        },
      ),
    );
  }
}
