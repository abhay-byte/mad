import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:app/bloc/form/form_bloc.dart';
import 'package:app/data/repositories/form_repository.dart';

class TestApp extends StatelessWidget {
  final Widget child;
  final FormRepository? repository;

  const TestApp({super.key, required this.child, this.repository});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: BlocProvider(
        create: (context) => FormBloc(repository: repository),
        child: child,
      ),
    );
  }
}
