import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../providers/workout_provider.dart';
import '../../../providers/user_provider.dart';
import '../../../utils/app_colors.dart';
import '../../../models/workout.dart';
import '../../../models/exercise_progress.dart';

/// Pantalla que muestra los planes de entrenamiento del usuario.
/// 
/// Permite navegar por los días de entrenamiento, ver detalles de ejercicios
/// y registrar el progreso (peso y repeticiones).
class WorkoutsScreen extends StatefulWidget {
  const WorkoutsScreen({super.key});

  @override
  State<WorkoutsScreen> createState() => _WorkoutsScreenState();
}

/// Estado de la pantalla de entrenamientos que gestiona la carga de planes.
class _WorkoutsScreenState extends State<WorkoutsScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _checkAndFetchPlans();
    });
  }

  void _checkAndFetchPlans() {
    final userProvider = context.read<UserProvider>();
    final workoutProvider = context.read<WorkoutProvider>();
    
    // Si el perfil ya está cargado, pedimos los planes
    if (userProvider.userProfile != null && userProvider.userProfile!.clientId != null) {
      workoutProvider.fetchClientWorkoutPlans(userProvider.userProfile!.clientId!);
    } else if (userProvider.userProfile == null && !userProvider.isLoading) {
      // Si no hay perfil, intentamos cargarlo primero
      userProvider.fetchProfile().then((_) {
        final updatedUser = context.read<UserProvider>().userProfile;
        if (updatedUser != null && updatedUser.clientId != null) {
          workoutProvider.fetchClientWorkoutPlans(updatedUser.clientId!);
        }
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: CustomScrollView(
        slivers: [
          SliverAppBar(
            expandedHeight: 120.0,
            floating: false,
            pinned: true,
            flexibleSpace: FlexibleSpaceBar(
              title: const Text(
                'Entrenamientos',
                style: TextStyle(
                  color: AppColors.text,
                  fontWeight: FontWeight.w900,
                ),
              ),
              centerTitle: true,
              background: Container(color: AppColors.background),
            ),
            backgroundColor: AppColors.background.withOpacity(0.8),
            elevation: 0,
          ),
          Consumer<WorkoutProvider>(
            builder: (context, workoutProvider, child) {
              if (workoutProvider.isLoading) {
                return const SliverFillRemaining(
                  child: Center(child: CircularProgressIndicator()),
                );
              }

              if (workoutProvider.errorMessage != null && workoutProvider.workoutPlans.isEmpty) {
                return SliverFillRemaining(
                  child: Center(
                    child: Padding(
                      padding: const EdgeInsets.all(32.0),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Icon(Icons.fitness_center_rounded, size: 64, color: AppColors.textMuted.withOpacity(0.2)),
                          const SizedBox(height: 16),
                          Text(
                            workoutProvider.errorMessage!,
                            textAlign: TextAlign.center,
                            style: const TextStyle(color: AppColors.textMuted),
                          ),
                        ],
                      ),
                    ),
                  ),
                );
              }

              return SliverPadding(
                padding: const EdgeInsets.all(16),
                sliver: SliverList(
                  delegate: SliverChildBuilderDelegate(
                    (context, index) {
                      final plan = workoutProvider.workoutPlans[index];
                      return _WorkoutPlanCard(plan: plan);
                    },
                    childCount: workoutProvider.workoutPlans.length,
                  ),
                ),
              );
            },
          ),
        ],
      ),
    );
  }
}

/// Tarjeta que representa un plan de entrenamiento completo.
class _WorkoutPlanCard extends StatelessWidget {
  final WorkoutPlan plan;

  const _WorkoutPlanCard({required this.plan});

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.only(bottom: 24),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(28),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.04),
            blurRadius: 24,
            offset: const Offset(0, 8),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Padding(
            padding: const EdgeInsets.all(24.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Expanded(
                      child: Text(
                        plan.title,
                        style: const TextStyle(
                          fontSize: 22,
                          fontWeight: FontWeight.w900,
                          color: AppColors.text,
                        ),
                      ),
                    ),
                    if (plan.active)
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                        decoration: BoxDecoration(
                          color: Colors.green.withOpacity(0.1),
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: const Text(
                          'ACTIVO',
                          style: TextStyle(
                            color: Colors.green,
                            fontSize: 10,
                            fontWeight: FontWeight.w900,
                          ),
                        ),
                      ),
                  ],
                ),
                if (plan.objective != null)
                  Padding(
                    padding: const EdgeInsets.only(top: 8.0),
                    child: Text(
                      plan.objective!,
                      style: TextStyle(
                        color: AppColors.textMuted.withOpacity(0.7),
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                  ),
              ],
            ),
          ),
          const Divider(height: 1, indent: 24, endIndent: 24),
          ...plan.workoutDays.map((day) => _WorkoutDayTile(day: day)),
          const SizedBox(height: 16),
        ],
      ),
    );
  }
}

/// Tile expandible que muestra los ejercicios de un día específico.
class _WorkoutDayTile extends StatelessWidget {
  final WorkoutDay day;

  const _WorkoutDayTile({required this.day});

  @override
  Widget build(BuildContext context) {
    return Theme(
      data: Theme.of(context).copyWith(dividerColor: Colors.transparent),
      child: ExpansionTile(
        title: Text(
          day.dayOfWeek,
          style: const TextStyle(
            fontWeight: FontWeight.w800,
            fontSize: 18,
            color: AppColors.text,
          ),
        ),
        subtitle: Text(
          day.focus ?? 'Enfoque general',
          style: TextStyle(color: AppColors.textMuted.withOpacity(0.6), fontSize: 13),
        ),
        leading: Container(
          padding: const EdgeInsets.all(10),
          decoration: BoxDecoration(
            color: AppColors.primary.withOpacity(0.1),
            shape: BoxShape.circle,
          ),
          child: const Icon(Icons.calendar_today_rounded, size: 18, color: AppColors.primary),
        ),
        childrenPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
        children: day.exercises.map((ex) => _ExerciseItem(exercise: ex)).toList(),
      ),
    );
  }
}

/// Elemento de lista interactivo para un ejercicio individual.
class _ExerciseItem extends StatelessWidget {
  final Exercise exercise;

  const _ExerciseItem({required this.exercise});

  void _showExerciseDetails(BuildContext context) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      builder: (context) => _ExerciseDetailSheet(exercise: exercise),
    );
  }

  @override
  Widget build(BuildContext context) {
    final completed = context.watch<WorkoutProvider>().completedExerciseNames.contains(exercise.name);

    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      decoration: BoxDecoration(
        color: completed ? Colors.green.withOpacity(0.08) : AppColors.background.withOpacity(0.5),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(
          color: completed ? Colors.green.withOpacity(0.2) : Colors.transparent,
          width: 1,
        ),
      ),
      child: ListTile(
        contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
        leading: Container(
          width: 48,
          height: 48,
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(12),
          ),
          child: ClipRRect(
            borderRadius: BorderRadius.circular(12),
            child: exercise.gifUrl != null && exercise.gifUrl!.isNotEmpty
                ? Image.network(
                    exercise.gifUrl!,
                    fit: BoxFit.cover,
                    errorBuilder: (context, error, stackTrace) => const Icon(Icons.fitness_center, color: AppColors.primary),
                  )
                : const Icon(Icons.fitness_center, color: AppColors.primary),
          ),
        ),
        title: Text(
          exercise.name,
          style: TextStyle(
            fontWeight: FontWeight.w800, 
            fontSize: 15,
            decoration: completed ? TextDecoration.lineThrough : null,
            color: completed ? AppColors.textMuted : AppColors.text,
          ),
        ),
        subtitle: Text(
          '${exercise.sets} series × ${exercise.reps} • ${exercise.restSeconds}s desc.',
          style: TextStyle(color: AppColors.textMuted.withOpacity(0.8), fontSize: 13),
        ),
        trailing: Container(
          padding: const EdgeInsets.all(8),
          decoration: BoxDecoration(
            color: completed ? Colors.green : Colors.white,
            shape: BoxShape.circle,
            boxShadow: [
              BoxShadow(
                color: Colors.black.withOpacity(0.05),
                blurRadius: 8,
              )
            ],
          ),
          child: Icon(
            completed ? Icons.check_rounded : Icons.play_arrow_rounded, 
            color: completed ? Colors.white : AppColors.primary, 
            size: 20,
          ),
        ),
        onTap: () => _showExerciseDetails(context),
      ),
    );
  }
}

/// Hoja inferior detallada para ver la técnica de un ejercicio y registrar progreso.
class _ExerciseDetailSheet extends StatefulWidget {
  final Exercise exercise;

  const _ExerciseDetailSheet({required this.exercise});

  @override
  State<_ExerciseDetailSheet> createState() => _ExerciseDetailSheetState();
}

class _ExerciseDetailSheetState extends State<_ExerciseDetailSheet> {
  final TextEditingController _weightController = TextEditingController();
  final TextEditingController _repsController = TextEditingController();
  bool _isSaving = false;

  Future<void> _saveProgress() async {
    if (_weightController.text.isEmpty || _repsController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Por favor, rellena todos los campos')),
      );
      return;
    }

    setState(() => _isSaving = true);

    final progress = ExerciseProgress(
      predefinedExerciseId: widget.exercise.predefinedExerciseId,
      exerciseName: widget.exercise.name,
      weight: double.parse(_weightController.text),
      reps: int.parse(_repsController.text),
      date: DateTime.now().toIso8601String().split('T')[0],
    );

    final success = await context.read<WorkoutProvider>().logProgress(progress);

    setState(() => _isSaving = false);

    if (success) {
      if (mounted) {
        Navigator.pop(context);
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Progreso registrado con éxito')),
        );
      }
    } else {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Error al registrar el progreso')),
        );
      }
    }
  }

  void _showHistory() async {
    final provider = context.read<WorkoutProvider>();
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('Historial: ${widget.exercise.name}', style: const TextStyle(fontWeight: FontWeight.w900)),
        content: FutureBuilder<List<ExerciseProgress>>(
          future: provider.getExerciseHistory(widget.exercise.name, predefinedId: widget.exercise.predefinedExerciseId),
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const SizedBox(height: 100, child: Center(child: CircularProgressIndicator()));
            }
            if (!snapshot.hasData || snapshot.data!.isEmpty) {
              return const Text('No hay registros previos.');
            }
            return SizedBox(
              width: double.maxFinite,
              child: ListView.builder(
                shrinkWrap: true,
                itemCount: snapshot.data!.length,
                itemBuilder: (context, index) {
                  final reg = snapshot.data![index];
                  return ListTile(
                    dense: true,
                    title: Text('${reg.weight} kg x ${reg.reps} reps', style: const TextStyle(fontWeight: FontWeight.w700)),
                    subtitle: Text(reg.date),
                    leading: const Icon(Icons.history_rounded, size: 20, color: AppColors.primary),
                  );
                },
              ),
            );
          },
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context), child: const Text('CERRAR'))
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return DraggableScrollableSheet(
      initialChildSize: 0.9,
      maxChildSize: 0.95,
      minChildSize: 0.5,
      builder: (context, scrollController) => Container(
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.vertical(top: Radius.circular(32)),
        ),
        child: SingleChildScrollView(
          controller: scrollController,
          padding: const EdgeInsets.fromLTRB(24, 12, 24, 24),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Center(
                child: Container(
                  width: 40,
                  height: 4,
                  decoration: BoxDecoration(
                    color: Colors.grey[300],
                    borderRadius: BorderRadius.circular(2),
                  ),
                ),
              ),
              const SizedBox(height: 24),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          widget.exercise.name,
                          style: const TextStyle(fontSize: 26, fontWeight: FontWeight.w900, letterSpacing: -0.5),
                        ),
                        Text(
                          'Técnica y Registro de Cargas',
                          style: TextStyle(color: AppColors.textMuted.withOpacity(0.6), fontWeight: FontWeight.w700, fontSize: 13),
                        ),
                      ],
                    ),
                  ),
                  IconButton(
                    onPressed: _showHistory,
                    icon: const Icon(Icons.history_rounded, color: AppColors.primary),
                    tooltip: 'Ver Historial',
                  ),
                  IconButton(
                    onPressed: () => Navigator.pop(context),
                    icon: const Icon(Icons.close_rounded, color: AppColors.textMuted),
                  ),
                ],
              ),
              const SizedBox(height: 24),
              // GIF View with Premium styling
              Container(
                height: 250,
                width: double.infinity,
                decoration: BoxDecoration(
                  color: AppColors.background,
                  borderRadius: BorderRadius.circular(28),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.05),
                      blurRadius: 20,
                      offset: const Offset(0, 10),
                    ),
                  ],
                ),
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(28),
                  child: widget.exercise.gifUrl != null && widget.exercise.gifUrl!.isNotEmpty
                      ? Image.network(
                          widget.exercise.gifUrl!,
                          fit: BoxFit.cover,
                          loadingBuilder: (context, child, loadingProgress) {
                            if (loadingProgress == null) return child;
                            return Center(
                              child: CircularProgressIndicator(
                                value: loadingProgress.expectedTotalBytes != null
                                    ? loadingProgress.cumulativeBytesLoaded / loadingProgress.expectedTotalBytes!
                                    : null,
                                strokeWidth: 2,
                                color: AppColors.primary.withOpacity(0.3),
                              ),
                            );
                          },
                          errorBuilder: (context, error, stackTrace) => const Center(
                            child: Icon(Icons.videocam_off_rounded, size: 48, color: Colors.grey),
                          ),
                        )
                      : const Center(
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Icon(Icons.fitness_center_rounded, size: 48, color: Colors.grey),
                              SizedBox(height: 8),
                              Text('No hay GIF disponible', style: TextStyle(color: Colors.grey, fontWeight: FontWeight.w600)),
                            ],
                          ),
                        ),
                ),
              ),
              const SizedBox(height: 32),
              Container(
                padding: const EdgeInsets.all(20),
                decoration: BoxDecoration(
                  color: AppColors.background.withOpacity(0.4),
                  borderRadius: BorderRadius.circular(24),
                  border: Border.all(color: Colors.black.withOpacity(0.02)),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Row(
                      children: [
                        Icon(Icons.edit_note_rounded, color: AppColors.primary, size: 20),
                        SizedBox(width: 8),
                        Text(
                          'REGISTRAR PROGRESO',
                          style: TextStyle(fontSize: 12, fontWeight: FontWeight.w900, letterSpacing: 0.5, color: AppColors.primary),
                        ),
                      ],
                    ),
                    const SizedBox(height: 20),
                    Row(
                      children: [
                        Expanded(
                          child: _buildInput(
                            label: 'PESO (KG)',
                            controller: _weightController,
                            icon: Icons.monitor_weight_outlined,
                            keyboardType: const TextInputType.numberWithOptions(decimal: true),
                            hint: '0.0',
                          ),
                        ),
                        const SizedBox(width: 16),
                        Expanded(
                          child: _buildInput(
                            label: 'REPETICIONES',
                            controller: _repsController,
                            icon: Icons.repeat_rounded,
                            keyboardType: TextInputType.number,
                            hint: '0',
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 32),
              AnimatedContainer(
                duration: const Duration(milliseconds: 300),
                child: ElevatedButton(
                  onPressed: _isSaving ? null : _saveProgress,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: AppColors.primary,
                    foregroundColor: Colors.white,
                    minimumSize: const Size(double.infinity, 64),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24)),
                    elevation: 8,
                    shadowColor: AppColors.primary.withOpacity(0.4),
                  ),
                  child: _isSaving
                      ? const SizedBox(
                          width: 24,
                          height: 24,
                          child: CircularProgressIndicator(color: Colors.white, strokeWidth: 2),
                        )
                      : const Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Text(
                              'GUARDAR PROGRESO',
                              style: TextStyle(fontWeight: FontWeight.w900, letterSpacing: 1, fontSize: 15),
                            ),
                            SizedBox(width: 12),
                            Icon(Icons.save_rounded, size: 20),
                          ],
                        ),
                ),
              ),
              if (widget.exercise.notes != null && widget.exercise.notes!.isNotEmpty) ...[
                const SizedBox(height: 32),
                const Text(
                  'NOTAS DEL ENTRENADOR',
                  style: TextStyle(fontWeight: FontWeight.w900, fontSize: 12, color: AppColors.textMuted, letterSpacing: 1),
                ),
                const SizedBox(height: 12),
                Container(
                  width: double.infinity,
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    color: Colors.amber.withOpacity(0.05),
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: Colors.amber.withOpacity(0.1)),
                  ),
                  child: Text(
                    widget.exercise.notes!,
                    style: const TextStyle(color: AppColors.text, fontWeight: FontWeight.w500, fontSize: 14),
                  ),
                ),
              ],
              const SizedBox(height: 40),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildInput({
    required String label,
    required TextEditingController controller,
    required IconData icon,
    TextInputType keyboardType = TextInputType.text,
    String hint = '',
  }) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          label,
          style: TextStyle(fontWeight: FontWeight.w900, fontSize: 10, color: AppColors.textMuted.withOpacity(0.8), letterSpacing: 0.5),
        ),
        const SizedBox(height: 8),
        TextField(
          controller: controller,
          keyboardType: keyboardType,
          style: const TextStyle(fontWeight: FontWeight.w800, fontSize: 18),
          decoration: InputDecoration(
            hintText: hint,
            prefixIcon: Icon(icon, size: 20, color: AppColors.primary.withOpacity(0.5)),
            filled: true,
            fillColor: Colors.white,
            contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 16),
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(16),
              borderSide: BorderSide(color: Colors.black.withOpacity(0.05)),
            ),
            enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(16),
              borderSide: BorderSide(color: Colors.black.withOpacity(0.05)),
            ),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(16),
              borderSide: const BorderSide(color: AppColors.primary, width: 2),
            ),
          ),
        ),
      ],
    );
  }
}

