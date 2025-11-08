import 'package:flutter_test/flutter_test.dart';
import 'package:bloc_test/bloc_test.dart';
import 'package:mockito/mockito.dart';
import 'package:app/bloc/form/form_bloc.dart';
import 'package:app/bloc/form/form_event.dart';
import 'package:app/bloc/form/form_state.dart';
import 'package:app/data/models/form_data.dart';
import '../mocks/form_repository_mock.mocks.dart';

void main() {
  group('FormBloc', () {
    late MockFormRepository mockRepository;
    late FormBloc formBloc;
    final testData = FormData(name: 'Test', email: 'test@example.com');

    setUp(() {
      mockRepository = MockFormRepository();
      formBloc = FormBloc(repository: mockRepository);

      // Set up default mock responses
      when(
        mockRepository.saveFormData(any),
      ).thenAnswer((_) => Future<void>.value());
      when(
        mockRepository.getFormData(),
      ).thenAnswer((_) => Future.value(testData));
    });

    tearDown(() {
      formBloc.close();
    });

    test('initial state is correct', () {
      expect(formBloc.state, equals(FormState.initial()));
    });

    blocTest<FormBloc, FormState>(
      'emits new state with updated name when NameChanged is added',
      build: () => formBloc,
      act: (bloc) => bloc.add(const NameChanged('John')),
      expect: () => [
        FormState(
          formData: const FormData(name: 'John', email: ''),
        ),
      ],
    );

    blocTest<FormBloc, FormState>(
      'emits new state with updated email when EmailChanged is added',
      build: () => formBloc,
      act: (bloc) => bloc.add(const EmailChanged('john@example.com')),
      expect: () => [
        FormState(
          formData: const FormData(name: '', email: 'john@example.com'),
        ),
      ],
    );

    blocTest<FormBloc, FormState>(
      'emits loading and success states when form is submitted successfully',
      build: () => formBloc,
      seed: () => FormState(formData: testData),
      act: (bloc) => bloc.add(const FormSubmitted()),
      expect: () => [
        FormState(formData: testData, isSubmitting: true),
        FormState(formData: testData, isSubmitting: false, isSuccess: true),
      ],
      verify: (_) {
        verify(mockRepository.saveFormData(testData)).called(1);
      },
    );

    blocTest<FormBloc, FormState>(
      'emits loading and error states when form submission fails',
      setUp: () {
        when(
          mockRepository.saveFormData(any),
        ).thenThrow(Exception('Failed to save'));
      },
      build: () => formBloc,
      seed: () => FormState(formData: testData),
      act: (bloc) => bloc.add(const FormSubmitted()),
      expect: () => [
        FormState(formData: testData, isSubmitting: true),
        FormState(
          formData: testData,
          isSubmitting: false,
          errorMessage: 'Failed to save form data',
        ),
      ],
    );

    blocTest<FormBloc, FormState>(
      'emits loaded data when FormLoaded is added',
      build: () => formBloc,
      act: (bloc) => bloc.add(const FormLoaded()),
      expect: () => [FormState(formData: testData)],
      verify: (_) {
        verify(mockRepository.getFormData()).called(1);
      },
    );
  });
}
