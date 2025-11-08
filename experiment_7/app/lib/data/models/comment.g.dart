// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'comment.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Comment _$CommentFromJson(Map<String, dynamic> json) => Comment(
  id: (json['id'] as num).toInt(),
  text: json['text'] as String?,
  time: (json['time'] as num).toInt(),
  by: json['by'] as String,
  kids: (json['kids'] as List<dynamic>).map((e) => (e as num).toInt()).toList(),
);

Map<String, dynamic> _$CommentToJson(Comment instance) => <String, dynamic>{
  'id': instance.id,
  'text': instance.text,
  'time': instance.time,
  'by': instance.by,
  'kids': instance.kids,
};
