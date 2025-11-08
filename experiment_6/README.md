# Experiment 6: Summary

### **Experiment No:**
6

### **Aim:**
To create a Flutter application demonstrating form handling with BLoC pattern, validation, and asynchronous operations.

---

### **Code:**

#### `lib/presentation/screens/form_screen.dart`

```dart
class _FormScreenState extends State<FormScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _emailController = TextEditingController();
  late final FormBloc _formBloc;

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) => _formBloc,
      child: BlocConsumer<FormBloc, form_state.FormState>(
        listener: (context, state) {
          if (state.isSuccess) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text('Form submitted successfully!')),
            );
            context.go('/');
          }
          if (state.errorMessage != null) {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(content: Text(state.errorMessage!)),
            );
          }
        },
        builder: (context, state) {
          return Scaffold(
            appBar: AppBar(
              title: const Text('Form Demo'),
              backgroundColor: Theme.of(context).colorScheme.inversePrimary,
            ),
            body: Form(
              key: _formKey,
              child: Column(
                children: [
                  TextFormField(
                    controller: _nameController,
                    decoration: const InputDecoration(
                      labelText: 'Name',
                      border: OutlineInputBorder(),
                    ),
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Please enter your name';
                      }
                      return null;
                    },
                  ),
                  TextFormField(
                    controller: _emailController,
                    decoration: const InputDecoration(
                      labelText: 'Email',
                      border: OutlineInputBorder(),
                    ),
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Please enter your email';
                      }
                      if (!value.contains('@')) {
                        return 'Please enter a valid email';
                      }
                      return null;
                    },
                  ),
                  BlocBuilder<FormBloc, form_state.FormState>(
                    builder: (context, state) {
                      return ElevatedButton(
                        onPressed: state.isSubmitting
                            ? null
                            : () {
                                if (_formKey.currentState!.validate()) {
                                  _formBloc.add(FormSubmitted());
                                }
                              },
                        child: state.isSubmitting
                            ? const CircularProgressIndicator()
                            : const Text('Submit'),
                      );
                    },
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
```

#### `lib/bloc/form/form_bloc.dart`

```dart
class FormBloc extends Bloc<FormEvent, FormState> {
  final _repository = FormRepository();

  FormBloc() : super(const FormState()) {
    on<FormLoaded>(_onFormLoaded);
    on<NameChanged>(_onNameChanged);
    on<EmailChanged>(_onEmailChanged);
    on<FormSubmitted>(_onFormSubmitted);
  }

  Future<void> _onFormSubmitted(
    FormSubmitted event,
    Emitter<FormState> emit,
  ) async {
    emit(state.copyWith(isSubmitting: true));
    try {
      await _repository.submitForm(state.formData);
      emit(state.copyWith(
        isSubmitting: false,
        isSuccess: true,
      ));
    } catch (e) {
      emit(state.copyWith(
        isSubmitting: false,
        errorMessage: e.toString(),
      ));
    }
  }
}
```

#### `lib/data/repositories/form_repository.dart`

```dart
class FormRepository {
  Future<void> submitForm(FormData formData) async {
    // Simulate network delay
    await Future.delayed(const Duration(seconds: 2));
    
    // Simulate validation
    if (formData.email.isEmpty || !formData.email.contains('@')) {
      throw Exception('Invalid email format');
    }
    
    // Simulate successful submission
    return;
  }
}
```

---

### **Output:**

The application displays a form with the following features:
1. A navigation bar with app title "Form Demo" and a back button
2. Two form fields:
   - Name field with required validation
   - Email field with required and email format validation
3. A submit button that shows:
   - Normal state: "Submit" text
   - Loading state: A circular progress indicator
   - Disabled state: When form is submitting
4. Validation feedback:
   - Error messages below each field when validation fails
   - Success message via SnackBar when form is submitted successfully
   - Error message via SnackBar when submission fails
5. Navigation:
   - Back button in AppBar returns to home screen
   - Automatic navigation to home screen after successful submission

---

### **Result:**

The experiment was successfully completed, implementing a fully functional form with BLoC pattern for state management, form validation, asynchronous operations simulation, and proper user feedback through loading states and error handling.