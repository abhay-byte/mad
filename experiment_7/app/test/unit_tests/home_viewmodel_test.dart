import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';
import 'package:mockito/annotations.dart';
import 'package:experiment_7/domain/repositories/hackernews_repository.dart';
import 'package:experiment_7/domain/models/story.dart';
import 'package:experiment_7/presentation/viewmodels/home_viewmodel.dart';
import '../mocks/hackernews_repository_mock.mocks.dart';

void main() {
  late HomeViewModel viewModel;
  late MockHackerNewsRepository mockRepository;

  setUp(() {
    mockRepository = MockHackerNewsRepository();
    viewModel = HomeViewModel(repository: mockRepository);
  });

  group('HomeViewModel Tests', () {
    final testStories = [
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

    test('initial state is correct', () {
      expect(viewModel.stories, null);
      expect(viewModel.error, null);
      expect(viewModel.isLoading, false);
    });

    test('loadTopStories updates state correctly on success', () async {
      // Arrange
      when(
        mockRepository.getTopStories(),
      ).thenAnswer((_) => Future.value(testStories));

      // Act
      await viewModel.loadTopStories();

      // Assert
      expect(viewModel.isLoading, false);
      expect(viewModel.error, null);
      expect(viewModel.stories, testStories);
      verify(mockRepository.getTopStories()).called(1);
    });

    test('loadTopStories updates state correctly on error', () async {
      // Arrange
      when(
        mockRepository.getTopStories(),
      ).thenAnswer((_) => Future.error(Exception('Failed to load')));

      // Act
      await viewModel.loadTopStories();

      // Assert
      expect(viewModel.isLoading, false);
      expect(viewModel.error, contains('Failed to load'));
      expect(viewModel.stories, null);
      verify(mockRepository.getTopStories()).called(1);
    });

    test('refreshStories calls loadTopStories', () async {
      // Arrange
      when(
        mockRepository.getTopStories(),
      ).thenAnswer((_) => Future.value(testStories));

      // Act
      await viewModel.refreshStories();

      // Assert
      verify(mockRepository.getTopStories()).called(1);
    });
  });
}
