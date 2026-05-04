class WorkoutPlan {
  final int id;
  final String title;
  final String? objective;
  final String? notes;
  final String? startDate;
  final String? endDate;
  final bool active;
  final List<WorkoutDay> workoutDays;

  WorkoutPlan({
    required this.id,
    required this.title,
    this.objective,
    this.notes,
    this.startDate,
    this.endDate,
    required this.active,
    required this.workoutDays,
  });

  factory WorkoutPlan.fromJson(Map<String, dynamic> json) {
    return WorkoutPlan(
      id: json['id'],
      title: json['title'],
      objective: json['objective'],
      notes: json['notes'],
      startDate: json['startDate'],
      endDate: json['endDate'],
      active: json['active'] ?? true,
      workoutDays: (json['workoutDays'] as List?)
              ?.map((d) => WorkoutDay.fromJson(d))
              .toList() ??
          [],
    );
  }
}

class WorkoutDay {
  final int id;
  final String dayOfWeek;
  final String? focus;
  final String? notes;
  final List<Exercise> exercises;

  WorkoutDay({
    required this.id,
    required this.dayOfWeek,
    this.focus,
    this.notes,
    required this.exercises,
  });

  factory WorkoutDay.fromJson(Map<String, dynamic> json) {
    return WorkoutDay(
      id: json['id'],
      dayOfWeek: json['dayOfWeek'] ?? '',
      focus: json['focus'],
      notes: json['notes'],
      exercises: (json['exercises'] as List?)
              ?.map((e) => Exercise.fromJson(e))
              .toList() ??
          [],
    );
  }
}

class Exercise {
  final int id;
  final String name;
  final int? sets;
  final String? reps;
  final int? restSeconds;
  final int? durationMinutes;
  final String? notes;

  Exercise({
    required this.id,
    required this.name,
    this.sets,
    this.reps,
    this.restSeconds,
    this.durationMinutes,
    this.notes,
  });

  factory Exercise.fromJson(Map<String, dynamic> json) {
    return Exercise(
      id: json['id'],
      name: json['name'],
      sets: json['sets'],
      reps: json['reps'],
      restSeconds: json['restSeconds'],
      durationMinutes: json['durationMinutes'],
      notes: json['notes'],
    );
  }
}
