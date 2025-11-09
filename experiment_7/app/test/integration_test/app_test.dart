import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';
import 'package:experiment_7/main.dart' as app;
import 'package:experiment_7/presentation/screens/story_list_screen.dart';

void main() {
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();

  group('End-to-end test', () {
    testWidgets('Loads stories and displays them', (tester) async {
      app.main();
      await tester.pumpAndSettle();

      // Verify we're on the story list screen
      expect(find.byType(StoryListScreen), findsOneWidget);

      // Wait for stories to load
      await tester.pumpAndSettle(const Duration(seconds: 5));

      // Should not show loading indicator anymore
      expect(find.byType(CircularProgressIndicator), findsNothing);

      // Should show at least one story
      expect(find.byType(StoryListItem), findsWidgets);

      // Test pull to refresh
      await tester.fling(find.byType(ListView), const Offset(0, 300), 1000);
      await tester.pumpAndSettle();

      // Should show stories again after refresh
      expect(find.byType(StoryListItem), findsWidgets);
    });
  });
}
