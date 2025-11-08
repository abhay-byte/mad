class Story {
  final int id;
  final String title;
  final String url;
  final int score;
  final int time;
  final String by;
  final List<int> kids;

  Story({
    required this.id,
    required this.title,
    required this.url,
    required this.score,
    required this.time,
    required this.by,
    required this.kids,
  });

  String get formattedTime {
    final dateTime = DateTime.fromMillisecondsSinceEpoch(time * 1000);
    final now = DateTime.now();
    final difference = now.difference(dateTime);

    if (difference.inDays > 0) {
      return '${difference.inDays}d ago';
    } else if (difference.inHours > 0) {
      return '${difference.inHours}h ago';
    } else if (difference.inMinutes > 0) {
      return '${difference.inMinutes}m ago';
    } else {
      return 'just now';
    }
  }

  factory Story.fromJson(Map<String, dynamic> json) {
    return Story(
      id: json['id'] as int,
      title: json['title'] as String,
      url: json['url'] as String? ?? '',
      score: json['score'] as int? ?? 0,
      time: json['time'] as int,
      by: json['by'] as String,
      kids: (json['kids'] as List?)?.map((e) => e as int).toList() ?? [],
    );
  }
}
