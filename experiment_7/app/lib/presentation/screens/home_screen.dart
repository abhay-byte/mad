import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../viewmodels/home_viewmodel.dart';
import '../widgets/story_card.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Hacker News'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: () {
              // TODO: Implement refresh
            },
          ),
        ],
      ),
      body: const StoriesList(),
    );
  }
}

class StoriesList extends StatelessWidget {
  const StoriesList({super.key});

  @override
  Widget build(BuildContext context) {
    final viewModel = context.watch<HomeViewModel>();

    switch (viewModel.status) {
      case HomeScreenStatus.loading:
        return const Center(child: CircularProgressIndicator());
      case HomeScreenStatus.error:
        return Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(viewModel.errorMessage ?? 'An error occurred'),
              const SizedBox(height: 16),
              ElevatedButton(
                onPressed: viewModel.refresh,
                child: const Text('Retry'),
              ),
            ],
          ),
        );
      case HomeScreenStatus.success:
        return RefreshIndicator(
          onRefresh: viewModel.refresh,
          child: CustomScrollView(
            physics: const BouncingScrollPhysics(
              parent: AlwaysScrollableScrollPhysics(),
            ),
            slivers: [
              SliverList(
                delegate: SliverChildBuilderDelegate((context, index) {
                  final story = viewModel.stories[index];
                  return StoryCard(
                    story: story,
                    onTap: () {
                      // TODO: Navigate to story details
                    },
                  );
                }, childCount: viewModel.stories.length),
              ),
            ],
          ),
        );
      case HomeScreenStatus.initial:
      default:
        return const SizedBox.shrink();
    }
  }
}
