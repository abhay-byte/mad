// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'story.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Story _$StoryFromJson(Map<String, dynamic> json) => Story(
  id: (json['id'] as num).toInt(),
  title: json['title'] as String,
  url: json['url'] as String?,
  score: (json['score'] as num).toInt(),
  time: (json['time'] as num).toInt(),
  by: json['by'] as String,
  kids: (json['kids'] as List<dynamic>).map((e) => (e as num).toInt()).toList(),
);

Map<String, dynamic> _$StoryToJson(Story instance) => <String, dynamic>{
  'id': instance.id,
  'title': instance.title,
  'url': instance.url,
  'score': instance.score,
  'time': instance.time,
  'by': instance.by,
  'kids': instance.kids,
};
