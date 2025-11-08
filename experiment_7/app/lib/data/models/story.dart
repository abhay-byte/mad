import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'story.g.dart';

@JsonSerializable()
class Story extends Equatable {
  final int id;
  final String title;
  final String? url;
  final int score;
  final int time;
  final String by;
  final List<int> kids;

  const Story({
    required this.id,
    required this.title,
    this.url,
    required this.score,
    required this.time,
    required this.by,
    required this.kids,
  });

  factory Story.fromJson(Map<String, dynamic> json) => _$StoryFromJson(json);

  Map<String, dynamic> toJson() => _$StoryToJson(this);

  @override
  List<Object?> get props => [id, title, url, score, time, by, kids];

  String get formattedTime {
    final date = DateTime.fromMillisecondsSinceEpoch(time * 1000);
    final now = DateTime.now();
    final difference = now.difference(date);

    if (difference.inDays > 365) {
      return '${(difference.inDays / 365).floor()}y';
    } else if (difference.inDays > 30) {
      return '${(difference.inDays / 30).floor()}mo';
    } else if (difference.inDays > 0) {
      return '${difference.inDays}d';
    } else if (difference.inHours > 0) {
      return '${difference.inHours}h';
    } else if (difference.inMinutes > 0) {
      return '${difference.inMinutes}m';
    } else {
      return 'now';
    }
  }
}
