import 'package:go_router/go_router.dart';
import '../screens/home_screen.dart';
import '../screens/form_screen.dart';

final GoRouter router = GoRouter(
  routes: [
    GoRoute(path: '/', builder: (context, state) => const HomeScreen()),
    GoRoute(path: '/form', builder: (context, state) => const FormScreen()),
  ],
);
