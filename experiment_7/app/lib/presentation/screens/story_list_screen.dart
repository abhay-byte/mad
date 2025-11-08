import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:experiment_7/presentation/viewmodels/home_viewmodel.dart';
import 'package:experiment_7/domain/models/story.dart';

class StoryListScreen extends StatelessWidget {
  const StoryListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('HackerNews Stories')),
      body: Consumer<HomeViewModel>(
        builder: (context, viewModel, child) {
          if (viewModel.isLoading) {
            return const Center(child: CircularProgressIndicator());
          }

          if (viewModel.error != null) {
            return Center(child: Text('Error: ${viewModel.error}'));
          }

          final stories = viewModel.stories;
          if (stories.isEmpty) {
            return const Center(child: Text('No stories found'));
          }

          return RefreshIndicator(
            onRefresh: viewModel.refreshStories,
            child: ListView.builder(
              itemCount: stories.length,
              itemBuilder: (context, index) {
                final story = stories[index];
                return StoryListItem(story: story);
              },
            ),
          );
        },
      ),
    );
  }
}

class StoryListItem extends StatelessWidget {
  final Story story;

  const StoryListItem({super.key, required this.story});

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 8.0, vertical: 4.0),
      child: ListTile(
        title: Text(story.title),
        subtitle: Text('by ${story.by} • ${story.score} points'),
        onTap: () {
          // Handle story tap - will implement navigation later
        },
      ),
    );
  }
}
