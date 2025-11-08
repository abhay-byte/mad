import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/form_data.dart';

class FormRepository {
  static const String _storageKey = 'form_data';

  Future<void> saveFormData(FormData formData) async {
    final prefs = await SharedPreferences.getInstance();
    final jsonData = {'name': formData.name, 'email': formData.email};
    await prefs.setString(_storageKey, jsonEncode(jsonData));
  }

  Future<FormData?> getFormData() async {
    final prefs = await SharedPreferences.getInstance();
    final jsonString = prefs.getString(_storageKey);
    if (jsonString != null) {
      final jsonData = jsonDecode(jsonString) as Map<String, dynamic>;
      return FormData(
        name: jsonData['name'] as String,
        email: jsonData['email'] as String,
      );
    }
    return null;
  }

  Future<void> clearFormData() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_storageKey);
  }
}
