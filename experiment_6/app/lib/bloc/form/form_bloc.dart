import 'package:flutter_bloc/flutter_bloc.dart';
import '../../data/models/form_data.dart';
import '../../data/repositories/form_repository.dart';
import 'form_event.dart';
import 'form_state.dart';

class FormBloc extends Bloc<FormEvent, FormState> {
  final FormRepository _repository;

  FormBloc({FormRepository? repository})
    : _repository = repository ?? FormRepository(),
      super(FormState.initial()) {
    on<NameChanged>(_onNameChanged);
    on<EmailChanged>(_onEmailChanged);
    on<FormSubmitted>(_onFormSubmitted);
    on<FormLoaded>(_onFormLoaded);
  }

  void _onNameChanged(NameChanged event, Emitter<FormState> emit) {
    final formData = state.formData.copyWith(name: event.name);
    emit(state.copyWith(formData: formData));
  }

  void _onEmailChanged(EmailChanged event, Emitter<FormState> emit) {
    final formData = state.formData.copyWith(email: event.email);
    emit(state.copyWith(formData: formData));
  }

  Future<void> _onFormLoaded(FormLoaded event, Emitter<FormState> emit) async {
    try {
      final formData = await _repository.getFormData();
      if (formData != null) {
        emit(state.copyWith(formData: formData));
      }
    } catch (error) {
      emit(state.copyWith(errorMessage: 'Failed to load saved data'));
    }
  }

  Future<void> _onFormSubmitted(
    FormSubmitted event,
    Emitter<FormState> emit,
  ) async {
    emit(state.copyWith(isSubmitting: true));
    try {
      await _repository.saveFormData(state.formData);
      emit(state.copyWith(isSubmitting: false, isSuccess: true));
    } catch (error) {
      emit(
        state.copyWith(
          isSubmitting: false,
          errorMessage: 'Failed to save form data',
        ),
      );
    }
  }
}
