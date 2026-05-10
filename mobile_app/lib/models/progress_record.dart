/// Modelo que representa un registro de progreso físico (peso, medidas) del cliente.
class ProgressRecord {
  final int? id;
  final int? clientId;
  final String? clientName;
  final String recordDate;
  final double? weight;
  final double? bodyFat;
  final double? chest;
  final double? waist;
  final double? hips;
  final double? arms;
  final double? legs;
  final String? notes;
  final String? createdAt;

  ProgressRecord({
    this.id,
    this.clientId,
    this.clientName,
    required this.recordDate,
    this.weight,
    this.bodyFat,
    this.chest,
    this.waist,
    this.hips,
    this.arms,
    this.legs,
    this.notes,
    this.createdAt,
  });

  factory ProgressRecord.fromJson(Map<String, dynamic> json) {
    return ProgressRecord(
      id: json['id'],
      clientId: json['clientId'],
      clientName: json['clientName'],
      recordDate: json['recordDate'],
      weight: json['weight'] != null ? (json['weight'] as num).toDouble() : null,
      bodyFat: json['bodyFat'] != null ? (json['bodyFat'] as num).toDouble() : null,
      chest: json['chest'] != null ? (json['chest'] as num).toDouble() : null,
      waist: json['waist'] != null ? (json['waist'] as num).toDouble() : null,
      hips: json['hips'] != null ? (json['hips'] as num).toDouble() : null,
      arms: json['arms'] != null ? (json['arms'] as num).toDouble() : null,
      legs: json['legs'] != null ? (json['legs'] as num).toDouble() : null,
      notes: json['notes'],
      createdAt: json['createdAt'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      'recordDate': recordDate,
      if (weight != null) 'weight': weight,
      if (bodyFat != null) 'bodyFat': bodyFat,
      if (chest != null) 'chest': chest,
      if (waist != null) 'waist': waist,
      if (hips != null) 'hips': hips,
      if (arms != null) 'arms': arms,
      if (legs != null) 'legs': legs,
      if (notes != null) 'notes': notes,
    };
  }
}
