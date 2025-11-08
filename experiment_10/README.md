# Experiment 10: IoT Control Application

### **Experiment No:**
10

### **Aim:**
To create an IoT control application using MQTT protocol for real-time device monitoring and control with Flutter.

### **Code:**

#### `lib/services/mqtt_service.dart`
```dart
class MQTTService {
  final MqttServerClient _client;
  final _connectionStateController = StreamController<MQTTConnectionState>.broadcast();
  final _messageController = StreamController<String>.broadcast();

  Stream<MQTTConnectionState> get connectionState => _connectionStateController.stream;
  Stream<String> get messageStream => _messageController.stream;

  // ...connection and message handling logic
}
```

#### `lib/bloc/iot_bloc.dart`
```dart
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

    _setupSubscriptions();
  }
}
```

#### `lib/ui/device_list_screen.dart`
```dart
class DeviceListScreen extends StatefulWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('IoT Device Control'),
        actions: [
          BlocBuilder<IoTBloc, IoTState>(
            builder: (context, state) {
              return _buildConnectionStatus(state.mqttState);
            },
          ),
        ],
      ),
      body: BlocBuilder<IoTBloc, IoTState>(
        builder: (context, state) {
          // Device list and error handling UI
        },
      ),
    );
  }
}
```

### **Output:**

The final application displays a list of IoT devices with:
1. Real-time connection status indicator
2. Device list with status and controls
3. Error handling with retry capability
4. Loading state during MQTT connection
5. Empty state when no devices are found

### **Result:**

Successfully implemented an IoT control application using:
- MQTT protocol for real-time communication
- BLoC pattern for state management
- Clean architecture with separation of concerns
- Robust error handling and state management
- Real-time device monitoring and control capabilities
