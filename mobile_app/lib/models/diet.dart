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
      meals: (json['meals'] as List?)
              ?.map((m) => DietMeal.fromJson(m))
              .toList() ??
          [],
    );
  }
}

class DietMeal {
  final int id;
  final String mealType;
  final String? mealTime;
  final String foods;
  final int? calories;
  final String? notes;

  DietMeal({
    required this.id,
    required this.mealType,
    this.mealTime,
    required this.foods,
    this.calories,
    this.notes,
  });

  factory DietMeal.fromJson(Map<String, dynamic> json) {
    return DietMeal(
      id: json['id'],
      mealType: json['mealType'],
      mealTime: json['mealTime'],
      foods: json['foods'] ?? '',
      calories: json['calories'],
      notes: json['notes'],
    );
  }
}
