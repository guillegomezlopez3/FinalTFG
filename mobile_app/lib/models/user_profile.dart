/// Respuesta que contiene la información completa del perfil del usuario (entrenador o cliente).
class UserProfileResponse {
  final int id;
  final String name;
  final String email;
  final String role;
  final bool active;
  final String? avatar;

  // Trainer data
  final int? trainerId;
  final String? phone;
  final String? specialty;
  final String? description;

  // Client data
  final int? clientId;
  final int? assignedTrainerId;
  final String? assignedTrainerName;

  final int? unreadMessagesCount;

  UserProfileResponse({
    required this.id,
    required this.name,
    required this.email,
    required this.role,
    required this.active,
    this.avatar,
    this.trainerId,
    this.phone,
    this.specialty,
    this.description,
    this.clientId,
    this.assignedTrainerId,
    this.assignedTrainerName,
    this.unreadMessagesCount,
  });

  factory UserProfileResponse.fromJson(Map<String, dynamic> json) {
    return UserProfileResponse(
      id: json['id'],
      name: json['name'],
      email: json['email'],
      role: json['role'] ?? 'CLIENT',
      active: json['active'] ?? true,
      avatar: json['avatar'],
      trainerId: json['trainerId'],
      phone: json['phone'],
      specialty: json['specialty'],
      description: json['description'],
      clientId: json['clientId'],
      assignedTrainerId: json['assignedTrainerId'],
      assignedTrainerName: json['assignedTrainerName'],
      unreadMessagesCount: json['unreadMessagesCount'] ?? 0,
    );
  }

  bool get isTrainer => role == 'TRAINER';
  bool get isClient => role == 'CLIENT';
}

