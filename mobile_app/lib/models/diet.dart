import 'dart:convert';

/// Modelo que representa un plan nutricional (Dieta).
class Diet {
  final int id;
  final String title;
  final String? description;
  final String? startDate;
  final String? endDate;
  final bool active;
  final List<DietMeal> meals;

  Diet({
    required this.id,
    required this.title,
    this.description,
    this.startDate,
    this.endDate,
    required this.active,
    required this.meals,
  });

  factory Diet.fromJson(Map<String, dynamic> json) {
    return Diet(
      id: json['id'],
      title: json['title'],
      description: json['description'],
      startDate: json['startDate'],
      endDate: json['endDate'],
      active: json['active'] ?? true,
      meals:
          (json['meals'] as List?)?.map((m) => DietMeal.fromJson(m)).toList() ??
          [],
    );
  }
}

/// Modelo que representa una comida individual dentro de una dieta.
class DietMeal {
  final int id;
  final String mealType;
  final String? mealTime;
  final String foods;
  final int? calories;
  final String? notes;
  final double? protein;
  final double? carbs;
  final double? fats;
  final bool completed;

  DietMeal({
    required this.id,
    required this.mealType,
    this.mealTime,
    required this.foods,
    this.calories,
    this.notes,
    this.protein,
    this.carbs,
    this.fats,
    this.completed = false,
  });

  String get formattedFoods {
    try {
      final parsed = jsonDecode(foods);
      if (parsed is List) {
        final buffer = StringBuffer();
        for (var option in parsed) {
          if (option is Map) {
            final optionName = option['name'] ?? 'Opción';
            buffer.writeln('• $optionName:');
            final items = option['items'];
            if (items is List) {
              for (var item in items) {
                if (item is Map) {
                  final name = item['name'] ?? '';
                  final grams = item['grams'] ?? '';
                  final kcal = item['kcal'] ?? '';
                  final notes = item['notes'] ?? '';

                  buffer.write('  - $name');
                  if (grams.toString().isNotEmpty) buffer.write(' ($grams)');
                  if (kcal.toString().isNotEmpty) buffer.write(' - $kcal kcal');
                  if (notes.toString().isNotEmpty) buffer.write(' ($notes)');
                  buffer.writeln();
                }
              }
            }
          }
        }
        return buffer.toString().trim();
      }
    } catch (_) {
      // Si no es un JSON válido, devolvemos el texto plano original
    }
    return foods;
  }

  factory DietMeal.fromJson(Map<String, dynamic> json) {
    return DietMeal(
      id: json['id'],
      mealType: json['mealType'],
      mealTime: json['mealTime'],
      foods: json['foods'] ?? '',
      calories: json['calories'],
      notes: json['notes'],
      protein: json['protein'] != null
          ? (json['protein'] as num).toDouble()
          : null,
      carbs: json['carbs'] != null ? (json['carbs'] as num).toDouble() : null,
      fats: json['fats'] != null ? (json['fats'] as num).toDouble() : null,
      completed: json['completed'] ?? false,
    );
  }
}
