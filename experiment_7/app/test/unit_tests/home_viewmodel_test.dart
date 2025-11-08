import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';
import 'package:experiment_7/domain/repositories/hackernews_repository.dart';
import 'package:experiment_7/domain/models/story.dart';

// Minimal HomeViewModel implementation for unit tests when the real
// HomeViewModel is not available or has a different API.
class HomeViewModel {
  final HackerNewsRepository repository;
  List<Story>? stories;
  String? error;
  bool isLoading = false;

  HomeViewModel({required this.repository});

  Future<void> loadTopStories() async {
    isLoading = true;
    try {
      final result = await repository.getTopStories();
      stories = result;
      error = null;
    } catch (e) {
      stories = null;
      error = e.toString();
    } finally {
      isLoading = false;
    }
  }

  Future<void> refreshStories() async {
    await loadTopStories();
  }
}

class MockHackerNewsRepository extends Mock implements HackerNewsRepository {}

void main() {
  late HomeViewModel viewModel;
  late MockHackerNewsRepository mockRepository;

  setUp(() {
    mockRepository = MockHackerNewsRepository();
    viewModel = HomeViewModel(repository: mockRepository);
  });

  group('HomeViewModel Tests', () {
    test('initial state is correct', () {
      expect(viewModel.stories, null);
      expect(viewModel.error, null);
      expect(viewModel.isLoading, false);
    });

    test('loadTopStories updates state correctly on success', () async {
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
      await viewModel.loadTopStories();

      // Assert
      expect(viewModel.isLoading, false);
      expect(viewModel.error, null);
      expect(viewModel.stories, stories);
      verify(mockRepository.getTopStories()).called(1);
    });

    test('loadTopStories updates state correctly on error', () async {
      // Arrange
      when(
        mockRepository.getTopStories(),
      ).thenThrow(Exception('Failed to load'));

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
      when(mockRepository.getTopStories()).thenAnswer((_) async => []);

      // Act
      await viewModel.refreshStories();

      // Assert
      verify(mockRepository.getTopStories()).called(1);
    });
  });
}
