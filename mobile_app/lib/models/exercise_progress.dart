/// Modelo para registrar el progreso (marcas) en un ejercicio específico.
class ExerciseProgress {
  final int? id;
  final int? clientId;
  final int? predefinedExerciseId;
  final String exerciseName;
  final double weight;
  final int reps;
  final String date;
  final String? createdAt;

  ExerciseProgress({
    this.id,
    this.clientId,
    this.predefinedExerciseId,
    required this.exerciseName,
    required this.weight,
    required this.reps,
    required this.date,
    this.createdAt,
  });

  factory ExerciseProgress.fromJson(Map<String, dynamic> json) {
    return ExerciseProgress(
      id: json['id'],
      clientId: json['clientId'],
      predefinedExerciseId: json['predefinedExerciseId'],
      exerciseName: json['exerciseName'] ?? '',
      weight: (json['weight'] as num).toDouble(),
      reps: json['reps'],
      date: json['date'],
      createdAt: json['createdAt'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      'predefinedExerciseId': predefinedExerciseId,
      'exerciseName': exerciseName,
      'weight': weight,
      'reps': reps,
      'date': date,
    };
  }
}
