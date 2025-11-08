import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';
import 'package:provider/provider.dart';
import 'package:experiment_7/presentation/screens/story_list_screen.dart';
import 'package:experiment_7/presentation/viewmodels/home_viewmodel.dart';
import 'package:experiment_7/domain/models/story.dart';
import 'package:experiment_7/domain/repositories/hackernews_repository.dart';

class MockHackerNewsRepository extends Mock implements HackerNewsRepository {}

void main() {
  late MockHackerNewsRepository mockRepository;

  setUp(() {
    mockRepository = MockHackerNewsRepository();
  });

  testWidgets('StoryListScreen shows loading indicator initially', (
    WidgetTester tester,
  ) async {
    // Arrange
    when(mockRepository.getTopStories()).thenAnswer((_) async => []);

    // Act
    await tester.pumpWidget(
      MaterialApp(
        home: ChangeNotifierProvider(
          create: (_) => HomeViewModel(repository: mockRepository),
          child: const StoryListScreen(),
        ),
      ),
    );

    // Assert
    expect(find.byType(CircularProgressIndicator), findsOneWidget);
  });

  testWidgets('StoryListScreen shows stories when loaded', (
    WidgetTester tester,
  ) async {
    // Arrange
    final stories = [
      Story(
        id: 1,
        title: 'Test Story',
        url: 'https://example.com',
        score: 100,
        time: 1636454400,
        by: 'testuser',
        kids: [],
      ),
    ];

    when(mockRepository.getTopStories()).thenAnswer((_) async => stories);

    // Act
    await tester.pumpWidget(
      MaterialApp(
        home: ChangeNotifierProvider(
          create: (_) => HomeViewModel(repository: mockRepository),
          child: const StoryListScreen(),
        ),
      ),
    );

    await tester.pump();

    // Assert
    expect(find.text('Test Story'), findsOneWidget);
  });

  testWidgets('StoryListScreen shows error message on failure', (
    WidgetTester tester,
  ) async {
    // Arrange
    when(
      mockRepository.getTopStories(),
    ).thenThrow(Exception('Failed to load stories'));

    // Act
    await tester.pumpWidget(
      MaterialApp(
        home: ChangeNotifierProvider(
          create: (_) => HomeViewModel(repository: mockRepository),
          child: const StoryListScreen(),
        ),
      ),
    );

    await tester.pump();

    // Assert
    expect(find.text('Error: Failed to load stories'), findsOneWidget);
  });
}
