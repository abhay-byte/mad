import 'package:experiment_7/domain/models/story.dart';
import 'package:experiment_7/domain/models/comment.dart';

abstract class HackerNewsRepository {
  Future<List<Story>> getTopStories();
  Future<Story> getStory(int id);
  Future<List<Comment>> getStoryComments(List<int> commentIds);
}
