import 'package:equatable/equatable.dart';

class FormData extends Equatable {
  final String name;
  final String email;

  const FormData({required this.name, required this.email});

  @override
  List<Object?> get props => [name, email];

  FormData copyWith({String? name, String? email}) {
    return FormData(name: name ?? this.name, email: email ?? this.email);
  }
}
