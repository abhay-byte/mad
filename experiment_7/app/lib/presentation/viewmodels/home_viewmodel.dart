import 'package:flutter/foundation.dart';
import 'package:experiment_7/domain/models/story.dart';
import 'package:experiment_7/domain/repositories/hackernews_repository.dart';

enum HomeScreenStatus { initial, loading, success, error }

class HomeViewModel extends ChangeNotifier {
  final HackerNewsRepository _repository;
  List<Story> _stories = [];
  HomeScreenStatus _status = HomeScreenStatus.initial;
  String? _errorMessage;

  HomeViewModel({required HackerNewsRepository repository})
    : _repository = repository {
    _loadStories();
  }

  List<Story> get stories => _stories;
  HomeScreenStatus get status => _status;
  String? get errorMessage => _errorMessage;
  bool get isLoading => status == HomeScreenStatus.loading;
  String? get error => errorMessage;

  Future<void> refreshStories() {
    return _loadStories();
  }

  Future<void> _loadStories() async {
    if (_status == HomeScreenStatus.loading) return;

    _status = HomeScreenStatus.loading;
    _errorMessage = null;
    notifyListeners();

    try {
      _stories = await _repository.getTopStories();
      _status = HomeScreenStatus.success;
    } catch (e) {
      _status = HomeScreenStatus.error;
      _errorMessage = e.toString();
    }

    notifyListeners();
  }

  Future<void> refresh() async {
    await _loadStories();
  }
}
