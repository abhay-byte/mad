import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:equatable/equatable.dart';

// Events
abstract class AppEvent extends Equatable {
  const AppEvent();

  @override
  List<Object> get props => [];
}

// States
abstract class AppState extends Equatable {
  const AppState();
  
  @override
  List<Object> get props => [];
}

class AppInitial extends AppState {}

// BLoC
class AppBloc extends Bloc<AppEvent, AppState> {
  AppBloc() : super(AppInitial()) {
    on<AppEvent>((event, emit) {
      // Event handlers will be implemented in the next phase
    });
  }
}