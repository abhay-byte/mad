class Comment {
  final int id;
  final String text;
  final int time;
  final String by;
  final List<int> kids;

  Comment({
    required this.id,
    required this.text,
    required this.time,
    required this.by,
    required this.kids,
  });

  factory Comment.fromJson(Map<String, dynamic> json) {
    return Comment(
      id: json['id'] as int,
      text: json['text'] as String? ?? '',
      time: json['time'] as int,
      by: json['by'] as String,
      kids: (json['kids'] as List?)?.map((e) => e as int).toList() ?? [],
    );
  }
}
