import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:experiment_7/domain/repositories/hackernews_repository.dart';
import 'package:experiment_7/data/repositories/hackernews_repository_impl.dart';
import 'package:experiment_7/data/services/hackernews_api_service.dart';
import 'package:experiment_7/presentation/screens/home_screen.dart';
import 'package:experiment_7/presentation/viewmodels/home_viewmodel.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        Provider<HackerNewsApiService>(create: (_) => HackerNewsApiService()),
        ProxyProvider<HackerNewsApiService, HackerNewsRepository>(
          update: (context, apiService, _) =>
              HackerNewsRepositoryImpl(apiService: apiService),
        ),
        ChangeNotifierProxyProvider<HackerNewsRepository, HomeViewModel>(
          create: (context) =>
              HomeViewModel(repository: context.read<HackerNewsRepository>()),
          update: (context, repository, previous) =>
              previous ?? HomeViewModel(repository: repository),
        ),
      ],
      child: MaterialApp(
        title: 'Hacker News',
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepOrange),
          useMaterial3: true,
        ),
        home: const HomeScreen(),
      ),
    );
  }
}
