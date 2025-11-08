import 'package:equatable/equatable.dart';
import '../../data/models/form_data.dart';

class FormState extends Equatable {
  final FormData formData;
  final bool isSubmitting;
  final bool isSuccess;
  final String? errorMessage;

  const FormState({
    required this.formData,
    this.isSubmitting = false,
    this.isSuccess = false,
    this.errorMessage,
  });

  factory FormState.initial() {
    return FormState(
      formData: const FormData(name: '', email: ''),
    );
  }

  FormState copyWith({
    FormData? formData,
    bool? isSubmitting,
    bool? isSuccess,
    String? errorMessage,
  }) {
    return FormState(
      formData: formData ?? this.formData,
      isSubmitting: isSubmitting ?? this.isSubmitting,
      isSuccess: isSuccess ?? this.isSuccess,
      errorMessage: errorMessage ?? this.errorMessage,
    );
  }

  @override
  List<Object?> get props => [formData, isSubmitting, isSuccess, errorMessage];
}
