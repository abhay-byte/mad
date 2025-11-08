import 'package:dio/dio.dart';
import '../models/story.dart';
import '../models/comment.dart';

class HackerNewsRepository {
  static const String _baseUrl = 'https://hacker-news.firebaseio.com/v0';
  final Dio _dio;

  HackerNewsRepository({Dio? dio}) : _dio = dio ?? Dio();

  Future<List<Story>> getTopStories({int limit = 30}) async {
    try {
      final response = await _dio.get('$_baseUrl/topstories.json');
      if (response.data == null) throw Exception('Failed to load top stories');

      final data = response.data;
      if (data is! List) {
        throw Exception(
          'Invalid response format for top stories: ${data.runtimeType}',
        );
      }

      final storyIds = data
          .take(limit)
          .map((id) => id is int ? id : int.tryParse(id.toString()))
          .where((id) => id != null)
          .cast<int>()
          .toList();

      if (storyIds.isEmpty) {
        throw Exception('No valid story IDs found');
      }

      final stories = await Future.wait(storyIds.map((id) => _getStory(id)));

      final validStories = stories.whereType<Story>().toList();
      if (validStories.isEmpty) {
        throw Exception('No valid stories found');
      }

      return validStories;
    } catch (e) {
      throw Exception('Failed to load top stories: $e');
    }
  }

  Future<Story?> _getStory(int id) async {
    try {
      final response = await _dio.get('$_baseUrl/item/$id.json');
      if (response.data == null) return null;

      return Story.fromJson(response.data);
    } catch (e) {
      return null;
    }
  }

  Future<List<Comment>> getComments(
    List<int> commentIds, {
    int limit = 30,
  }) async {
    if (commentIds.isEmpty) return [];

    try {
      final limitedIds = commentIds.take(limit);
      final comments = await Future.wait(
        limitedIds.map((id) => _getComment(id)),
      );

      final validComments = comments.whereType<Comment>().toList();
      if (validComments.isEmpty) {
        throw Exception('Failed to load comments: No valid comments found');
      }

      return validComments;
    } catch (e) {
      throw Exception('Failed to load comments: $e');
    }
  }

  Future<Comment?> _getComment(int id) async {
    try {
      final response = await _dio.get('$_baseUrl/item/$id.json');
      if (response.data == null) return null;

      return Comment.fromJson(response.data);
    } catch (e) {
      return null;
    }
  }
}
