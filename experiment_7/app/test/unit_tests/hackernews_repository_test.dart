import 'package:mockito/mockito.dart';
import 'package:test/test.dart';
import 'package:experiment_7/data/repositories/hackernews_repository_impl.dart';
import 'package:experiment_7/data/services/hackernews_api_service.dart';
import 'package:experiment_7/domain/models/story.dart';
import 'package:experiment_7/domain/models/comment.dart';
import 'package:mockito/annotations.dart';

@GenerateNiceMocks([MockSpec<HackerNewsApiService>()])
import 'hackernews_repository_test.mocks.dart';

void main() {
  late HackerNewsRepositoryImpl repository;
  late MockHackerNewsApiService mockApiService;

  setUp(() {
    mockApiService = MockHackerNewsApiService();
    repository = HackerNewsRepositoryImpl(apiService: mockApiService);
  });

  group('HackerNewsRepository Tests', () {
    test('getTopStories returns list of Story objects', () async {
      // Arrange
      final List<int> storyIds = [1, 2, 3];
      final Map<String, dynamic> storyData = {
        'id': 1,
        'title': 'Test Story',
        'url': 'https://example.com',
        'score': 100,
        'time': 1636454400,
        'by': 'testuser',
        'kids': [4, 5, 6],
      };

      when(mockApiService.getTopStories()).thenAnswer((_) async => storyIds);
      when(mockApiService.getStory(any)).thenAnswer((_) async => storyData);

      // Act
      final result = await repository.getTopStories();

      // Assert
      expect(result.length, 3);
      expect(result.first, isA<Story>());
      expect(result.first.title, 'Test Story');
      verify(mockApiService.getTopStories()).called(1);
      verify(mockApiService.getStory(1)).called(1);
    });

    test('getStory returns Story object', () async {
      // Arrange
      const int storyId = 1;
      final Map<String, dynamic> storyData = {
        'id': storyId,
        'title': 'Test Story',
        'url': 'https://example.com',
        'score': 100,
        'time': 1636454400,
        'by': 'testuser',
        'kids': [4, 5, 6],
      };

      when(mockApiService.getStory(storyId)).thenAnswer((_) async => storyData);

      // Act
      final result = await repository.getStory(storyId);

      // Assert
      expect(result, isA<Story>());
      expect(result.id, storyId);
      expect(result.title, 'Test Story');
      verify(mockApiService.getStory(storyId)).called(1);
    });

    test('getStoryComments returns list of Comment objects', () async {
      // Arrange
      final List<int> commentIds = [4, 5, 6];
      final Map<String, dynamic> commentData = {
        'id': 4,
        'text': 'Test Comment',
        'time': 1636454400,
        'by': 'commenter',
        'kids': [],
      };

      when(mockApiService.getComment(any)).thenAnswer((_) async => commentData);

      // Act
      final result = await repository.getStoryComments(commentIds);

      // Assert
      expect(result.length, 3);
      expect(result.first.id, 4);
      expect(result.first.text, 'Test Comment');
      verify(mockApiService.getComment(4)).called(1);
    });
  });
}
