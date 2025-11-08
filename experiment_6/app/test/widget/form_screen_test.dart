import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';
import 'package:app/presentation/screens/form_screen.dart';
import '../helpers/test_app.dart';
import '../mocks/form_repository_mock.mocks.dart';

void main() {
  late MockFormRepository mockRepository;

  setUp(() {
    mockRepository = MockFormRepository();
    when(
      mockRepository.saveFormData(any),
    ).thenAnswer((_) => Future<void>.value());
  });

  testWidgets('FormScreen validates and submits form correctly', (
    WidgetTester tester,
  ) async {
    await tester.pumpWidget(
      TestApp(repository: mockRepository, child: const FormScreen()),
    );

    // Initially form should be empty
    expect(find.text(''), findsNWidgets(2)); // Two empty text fields

    // Fill in the name field first
    await tester.enterText(find.byType(TextFormField).first, 'John Doe');
    await tester.pump();

    // Fill in invalid email and submit
    await tester.enterText(find.byType(TextFormField).last, 'invalid-email');
    await tester.pump();

    // Tap submit button and wait for animations
    await tester.tap(find.text('Submit'));
    await tester.pump();
    await tester.pump(const Duration(milliseconds: 100));

    // Enter valid email
    await tester.enterText(find.byType(TextFormField).last, 'john@example.com');
    await tester.pump();

    // Tap submit button again
    await tester.tap(find.text('Submit'));
    await tester.pump();

    // Wait for the animations and save operation to complete
    await tester.pump(const Duration(milliseconds: 100));

    // Verify the save was called with correct data
    verify(mockRepository.saveFormData(any)).called(1);

    // Should show success indicator or message
    expect(find.byType(CircularProgressIndicator), findsNothing);
  });

  testWidgets('FormScreen shows loading indicator while submitting', (
    WidgetTester tester,
  ) async {
    // Setup mock to delay the response
    when(
      mockRepository.saveFormData(any),
    ).thenAnswer((_) => Future.delayed(const Duration(seconds: 1)));

    await tester.pumpWidget(
      TestApp(repository: mockRepository, child: const FormScreen()),
    );

    // Fill in valid data
    await tester.enterText(find.byType(TextFormField).first, 'John Doe');
    await tester.enterText(find.byType(TextFormField).last, 'john@example.com');
    await tester.pump();

    // Tap submit button
    await tester.tap(find.text('Submit'));
    await tester.pump();

    // Should show loading indicator
    expect(find.byType(CircularProgressIndicator), findsOneWidget);
  });
}
