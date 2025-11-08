import 'dart:convert';
import 'package:http/http.dart' as http;

class HackerNewsApiService {
  static const String _baseUrl = 'https://hacker-news.firebaseio.com/v0';

  Future<List<int>> getTopStories() async {
    final response = await http.get(Uri.parse('$_baseUrl/topstories.json'));
    if (response.statusCode == 200) {
      return List<int>.from(json.decode(response.body));
    }
    throw Exception('Failed to load top stories');
  }

  Future<Map<String, dynamic>> getStory(int id) async {
    final response = await http.get(Uri.parse('$_baseUrl/item/$id.json'));
    if (response.statusCode == 200) {
      return json.decode(response.body);
    }
    throw Exception('Failed to load story');
  }

  Future<Map<String, dynamic>> getComment(int id) async {
    final response = await http.get(Uri.parse('$_baseUrl/item/$id.json'));
    if (response.statusCode == 200) {
      return json.decode(response.body);
    }
    throw Exception('Failed to load comment');
  }
}
