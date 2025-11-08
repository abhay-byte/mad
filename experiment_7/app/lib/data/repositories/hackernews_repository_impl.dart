import 'package:experiment_7/data/services/hackernews_api_service.dart';
import 'package:experiment_7/domain/models/story.dart';
import 'package:experiment_7/domain/models/comment.dart';
import 'package:experiment_7/domain/repositories/hackernews_repository.dart';

class HackerNewsRepositoryImpl implements HackerNewsRepository {
  final HackerNewsApiService apiService;

  HackerNewsRepositoryImpl({required this.apiService});

  @override
  Future<List<Story>> getTopStories() async {
    try {
      final storyIds = await apiService.getTopStories();
      final stories = await Future.wait(
        storyIds.take(10).map((id) => getStory(id)).toList(),
      );
      return stories;
    } catch (e) {
      throw Exception('Failed to get top stories: $e');
    }
  }

  @override
  Future<Story> getStory(int id) async {
    try {
      final storyData = await apiService.getStory(id);
      return Story.fromJson(storyData);
    } catch (e) {
      throw Exception('Failed to get story: $e');
    }
  }

  @override
  Future<List<Comment>> getStoryComments(List<int> commentIds) async {
    try {
      final comments = await Future.wait(
        commentIds.take(10).map((id) => _getComment(id)).toList(),
      );
      return comments;
    } catch (e) {
      throw Exception('Failed to get comments: $e');
    }
  }

  Future<Comment> _getComment(int id) async {
    final commentData = await apiService.getComment(id);
    return Comment.fromJson(commentData);
  }
}
