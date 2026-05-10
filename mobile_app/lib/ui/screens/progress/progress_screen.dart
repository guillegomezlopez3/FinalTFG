import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../providers/progress_provider.dart';
import '../../../models/progress_record.dart';
import '../../../utils/app_colors.dart';
import '../../../providers/user_provider.dart';

/// Pantalla que muestra la evolución física del cliente.
/// 
/// Incluye una gráfica de peso y un historial de registros antropométricos.
class ProgressScreen extends StatefulWidget {
  const ProgressScreen({super.key});

  @override
  State<ProgressScreen> createState() => _ProgressScreenState();
}

/// Estado de la pantalla de progreso que gestiona la carga inicial de registros.
class _ProgressScreenState extends State<ProgressScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<ProgressProvider>().fetchMyRecords();
    });
  }

  void _showAddProgressSheet() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (context) => const _AddProgressSheet(),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: Consumer<ProgressProvider>(
        builder: (context, provider, child) {
          return CustomScrollView(
            slivers: [
              _buildSliverAppBar(context),
              if (provider.isLoading && provider.records.isEmpty)
                const SliverFillRemaining(child: Center(child: CircularProgressIndicator())),
              
              if (provider.errorMessage != null && provider.records.isEmpty)
                SliverFillRemaining(child: Center(child: Text(provider.errorMessage!))),

              if (provider.records.isEmpty && !provider.isLoading)
                const SliverFillRemaining(
                  child: Center(
                    child: Text(
                      'No hay registros de progreso aún.\n¡Añade uno nuevo!',
                      textAlign: TextAlign.center,
                      style: TextStyle(color: AppColors.textMuted),
                    ),
                  ),
                ),

              if (provider.records.isNotEmpty) ...[
                SliverToBoxAdapter(
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(24, 32, 24, 16),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          'HISTORIAL',
                          style: TextStyle(fontWeight: FontWeight.w900, fontSize: 12, letterSpacing: 1, color: AppColors.textMuted),
                        ),
                        Text(
                          '${provider.records.length} registros',
                          style: const TextStyle(fontWeight: FontWeight.w700, fontSize: 12, color: AppColors.primary),
                        ),
                      ],
                    ),
                  ),
                ),
                SliverPadding(
                  padding: const EdgeInsets.symmetric(horizontal: 20),
                  sliver: SliverList(
                    delegate: SliverChildBuilderDelegate(
                      (context, index) {
                        final record = provider.records[index];
                        return _ProgressCard(record: record);
                      },
                      childCount: provider.records.length,
                    ),
                  ),
                ),
              ],
              const SliverToBoxAdapter(child: SizedBox(height: 100)),
            ],
          );
        },
      ),
      floatingActionButton: context.watch<UserProvider>().userProfile?.role == 'CLIENT' 
        ? null 
        : FloatingActionButton.extended(
            onPressed: _showAddProgressSheet,
            backgroundColor: AppColors.primary,
            elevation: 8,
            icon: const Icon(Icons.add_rounded, color: Colors.white),
            label: const Text('NUEVO REGISTRO', style: TextStyle(fontWeight: FontWeight.w900, letterSpacing: 0.5, color: Colors.white)),
          ),
    );
  }

  Widget _buildSliverAppBar(BuildContext context) {
    final provider = context.watch<ProgressProvider>();
    final records = provider.records;
    final lastWeight = records.isNotEmpty ? (records.first.weight ?? 0.0) : 0.0;
    
    return SliverAppBar(
      expandedHeight: 340,
      pinned: true,
      backgroundColor: AppColors.primary,
      elevation: 0,
      leading: IconButton(
        icon: const Icon(Icons.arrow_back_ios_new_rounded, color: Colors.white, size: 20),
        onPressed: () => Navigator.pop(context),
      ),
      flexibleSpace: FlexibleSpaceBar(
        background: Container(
          decoration: const BoxDecoration(
            gradient: LinearGradient(
              begin: Alignment.topRight,
              end: Alignment.bottomLeft,
              colors: [AppColors.primary, Color(0xFF6C63FF)],
            ),
          ),
          child: Stack(
            children: [
              Positioned(
                top: -30,
                right: -30,
                child: Container(
                  width: 150,
                  height: 150,
                  decoration: BoxDecoration(
                    color: Colors.white.withOpacity(0.05),
                    shape: BoxShape.circle,
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.fromLTRB(24, 80, 24, 20),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      'Tu Evolución',
                      style: TextStyle(color: Colors.white, fontSize: 28, fontWeight: FontWeight.w900, letterSpacing: -0.5),
                    ),
                    const SizedBox(height: 8),
                    Row(
                      crossAxisAlignment: CrossAxisAlignment.end,
                      children: [
                        Text(
                          lastWeight > 0 ? '$lastWeight' : '--',
                          style: const TextStyle(color: Colors.white, fontSize: 42, fontWeight: FontWeight.w900),
                        ),
                        const Padding(
                          padding: EdgeInsets.only(bottom: 8.0, left: 4),
                          child: Text('kg', style: TextStyle(color: Colors.white70, fontSize: 18, fontWeight: FontWeight.w700)),
                        ),
                        const Spacer(),
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                          decoration: BoxDecoration(
                            color: Colors.white.withOpacity(0.15),
                            borderRadius: BorderRadius.circular(20),
                          ),
                          child: const Row(
                            children: [
                              Icon(Icons.trending_down_rounded, color: Colors.white, size: 16),
                              SizedBox(width: 4),
                              Text('-1.2kg este mes', style: TextStyle(color: Colors.white, fontSize: 12, fontWeight: FontWeight.w800)),
                            ],
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 32),
                    // Small minimalist graph
                    Expanded(
                      child: Container(
                        width: double.infinity,
                        margin: const EdgeInsets.only(bottom: 10),
                        child: records.length > 1 
                          ? CustomPaint(painter: _WeightChartPainter(records))
                          : Center(child: Text('Añade más registros para ver la gráfica', style: TextStyle(color: Colors.white.withOpacity(0.5), fontSize: 12))),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

/// Tarjeta que muestra un registro de progreso individual con sus medidas.
class _ProgressCard extends StatelessWidget {
  final ProgressRecord record;

  const _ProgressCard({required this.record});

  String _formatDate(String isoDate) {
    final dateObj = DateTime.tryParse(isoDate);
    if (dateObj == null) return isoDate;
    
    const months = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
    final day = dateObj.day.toString().padLeft(2, '0');
    final month = months[dateObj.month - 1];
    return '$day $month';
  }

  @override
  Widget build(BuildContext context) {
    final displayDate = _formatDate(record.recordDate);

    return Container(
      margin: const EdgeInsets.only(bottom: 16),
      decoration: BoxDecoration(
        color: Colors.white.withOpacity(0.8),
        borderRadius: BorderRadius.circular(24),
        border: Border.all(color: Colors.white.withOpacity(0.5)),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.03),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          children: [
            Row(
              children: [
                Container(
                  padding: const EdgeInsets.all(12),
                  decoration: BoxDecoration(
                    color: AppColors.primary.withOpacity(0.1),
                    borderRadius: BorderRadius.circular(16),
                  ),
                  child: Text(displayDate, style: const TextStyle(fontWeight: FontWeight.w900, color: AppColors.primary)),
                ),
                const Spacer(),
                if (record.weight != null)
                  Text('${record.weight} kg', style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 18)),
              ],
            ),
            const Padding(
              padding: EdgeInsets.symmetric(vertical: 16),
              child: Divider(),
            ),
            Wrap(
              spacing: 24,
              runSpacing: 16,
              children: [
                if (record.bodyFat != null) _MeasurementItem(label: 'Grasa', value: '${record.bodyFat}%', icon: Icons.percent_rounded),
                if (record.chest != null) _MeasurementItem(label: 'Pecho', value: '${record.chest} cm', icon: Icons.straighten_rounded),
                if (record.waist != null) _MeasurementItem(label: 'Cintura', value: '${record.waist} cm', icon: Icons.circle_outlined),
                if (record.hips != null) _MeasurementItem(label: 'Cadera', value: '${record.hips} cm', icon: Icons.accessibility_new_rounded),
                if (record.arms != null) _MeasurementItem(label: 'Brazos', value: '${record.arms} cm', icon: Icons.fitness_center_rounded),
                if (record.legs != null) _MeasurementItem(label: 'Piernas', value: '${record.legs} cm', icon: Icons.directions_run_rounded),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

/// Widget para mostrar una medida individual (pecho, cintura, etc.) con icono.
class _MeasurementItem extends StatelessWidget {
  final String label;
  final String value;
  final IconData icon;

  const _MeasurementItem({required this.label, required this.value, required this.icon});

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Icon(icon, size: 14, color: AppColors.primary.withOpacity(0.5)),
        const SizedBox(width: 6),
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(label, style: TextStyle(fontSize: 8, color: AppColors.textMuted.withOpacity(0.5), fontWeight: FontWeight.w900, letterSpacing: 0.5)),
              Text(value, style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 13, color: AppColors.text)),
            ],
          ),
        ),
      ],
    );
  }
}

/// Pintor personalizado para dibujar la gráfica lineal de evolución de peso.
class _WeightChartPainter extends CustomPainter {
  final List<ProgressRecord> records;

  _WeightChartPainter(this.records);

  @override
  void paint(Canvas canvas, Size size) {
    if (records.length < 2) return;

    final paint = Paint()
      ..color = Colors.white
      ..strokeWidth = 3
      ..style = PaintingStyle.stroke
      ..strokeCap = StrokeCap.round;

    final fillPaint = Paint()
      ..shader = LinearGradient(
        begin: Alignment.topCenter,
        end: Alignment.bottomCenter,
        colors: [Colors.white.withOpacity(0.3), Colors.white.withOpacity(0.0)],
      ).createShader(Rect.fromLTWH(0, 0, size.width, size.height));

    final path = Path();
    final fillPath = Path();

    final filteredRecords = records.where((r) => r.weight != null).toList().reversed.toList();
    if (filteredRecords.length < 2) return;

    double minWeight = filteredRecords.map((r) => r.weight!).reduce((a, b) => a < b ? a : b) - 2;
    double maxWeight = filteredRecords.map((r) => r.weight!).reduce((a, b) => a > b ? a : b) + 2;
    double weightRange = maxWeight - minWeight;

    double xStep = size.width / (filteredRecords.length - 1);

    for (int i = 0; i < filteredRecords.length; i++) {
      double x = i * xStep;
      double y = size.height - ((filteredRecords[i].weight! - minWeight) / weightRange * size.height);

      if (i == 0) {
        path.moveTo(x, y);
        fillPath.moveTo(x, size.height);
        fillPath.lineTo(x, y);
      } else {
        path.lineTo(x, y);
        fillPath.lineTo(x, y);
      }
      
      if (i == filteredRecords.length - 1) {
        fillPath.lineTo(x, size.height);
        fillPath.close();
      }
    }

    canvas.drawPath(fillPath, fillPaint);
    canvas.drawPath(path, paint);

    // Draw points
    final dotPaint = Paint()..color = Colors.white;
    for (int i = 0; i < filteredRecords.length; i++) {
      double x = i * xStep;
      double y = size.height - ((filteredRecords[i].weight! - minWeight) / weightRange * size.height);
      canvas.drawCircle(Offset(x, y), 4, dotPaint);
    }
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => true;
}

/// Hoja inferior con formulario para añadir un nuevo registro de progreso.
class _AddProgressSheet extends StatefulWidget {
  const _AddProgressSheet();

  @override
  State<_AddProgressSheet> createState() => _AddProgressSheetState();
}

class _AddProgressSheetState extends State<_AddProgressSheet> {
  final _formKey = GlobalKey<FormState>();
  final _weightCtrl = TextEditingController();
  final _bodyFatCtrl = TextEditingController();
  final _chestCtrl = TextEditingController();
  final _waistCtrl = TextEditingController();
  final _hipsCtrl = TextEditingController();
  final _armsCtrl = TextEditingController();
  final _legsCtrl = TextEditingController();
  final _notesCtrl = TextEditingController();

  DateTime _selectedDate = DateTime.now();

  @override
  void dispose() {
    _weightCtrl.dispose();
    _bodyFatCtrl.dispose();
    _chestCtrl.dispose();
    _waistCtrl.dispose();
    _hipsCtrl.dispose();
    _armsCtrl.dispose();
    _legsCtrl.dispose();
    _notesCtrl.dispose();
    super.dispose();
  }

  String _formatIsoDate(DateTime date) {
    return '${date.year}-${date.month.toString().padLeft(2, '0')}-${date.day.toString().padLeft(2, '0')}';
  }

  String _formatDisplayDate(DateTime date) {
    return '${date.day.toString().padLeft(2, '0')}/${date.month.toString().padLeft(2, '0')}/${date.year}';
  }

  void _submit() async {
    if (!_formKey.currentState!.validate()) return;

    final record = ProgressRecord(
      recordDate: _formatIsoDate(_selectedDate),
      weight: double.tryParse(_weightCtrl.text),
      bodyFat: double.tryParse(_bodyFatCtrl.text),
      chest: double.tryParse(_chestCtrl.text),
      waist: double.tryParse(_waistCtrl.text),
      hips: double.tryParse(_hipsCtrl.text),
      arms: double.tryParse(_armsCtrl.text),
      legs: double.tryParse(_legsCtrl.text),
      notes: _notesCtrl.text.isNotEmpty ? _notesCtrl.text : null,
    );

    final success = await context.read<ProgressProvider>().addRecord(record);
    if (success && mounted) {
      Navigator.pop(context); // Close sheet
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Registro añadido correctamente')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    // Determine the bottom padding for the keyboard
    final bottomInset = MediaQuery.of(context).viewInsets.bottom;

    return Padding(
      padding: EdgeInsets.only(bottom: bottomInset, left: 24, right: 24, top: 24),
      child: SingleChildScrollView(
        child: Form(
          key: _formKey,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              const Text(
                'Nuevo Registro',
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 24),
              
              // Date Selector
              InkWell(
                onTap: () async {
                  final picked = await showDatePicker(
                    context: context,
                    initialDate: _selectedDate,
                    firstDate: DateTime(2000),
                    lastDate: DateTime.now(),
                  );
                  if (picked != null) {
                    setState(() => _selectedDate = picked);
                  }
                },
                child: InputDecorator(
                  decoration: const InputDecoration(
                    labelText: 'Fecha',
                    border: OutlineInputBorder(),
                  ),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(_formatDisplayDate(_selectedDate)),
                      const Icon(Icons.calendar_today, size: 20),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 16),
              
              Row(
                children: [
                  Expanded(
                    child: TextFormField(
                      controller: _weightCtrl,
                      decoration: const InputDecoration(labelText: 'Peso (kg)', border: OutlineInputBorder()),
                      keyboardType: const TextInputType.numberWithOptions(decimal: true),
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: TextFormField(
                      controller: _bodyFatCtrl,
                      decoration: const InputDecoration(labelText: '% Grasa', border: OutlineInputBorder()),
                      keyboardType: const TextInputType.numberWithOptions(decimal: true),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16),

              const Text('Medidas (cm)', style: TextStyle(fontWeight: FontWeight.w600)),
              const SizedBox(height: 8),
              
              Row(
                children: [
                  Expanded(child: _buildNumberField(_chestCtrl, 'Pecho')),
                  const SizedBox(width: 8),
                  Expanded(child: _buildNumberField(_waistCtrl, 'Cintura')),
                  const SizedBox(width: 8),
                  Expanded(child: _buildNumberField(_hipsCtrl, 'Cadera')),
                ],
              ),
              const SizedBox(height: 8),
              Row(
                children: [
                  Expanded(child: _buildNumberField(_armsCtrl, 'Brazos')),
                  const SizedBox(width: 8),
                  Expanded(child: _buildNumberField(_legsCtrl, 'Piernas')),
                  const Spacer(),
                ],
              ),
              const SizedBox(height: 16),

              TextFormField(
                controller: _notesCtrl,
                decoration: const InputDecoration(
                  labelText: 'Notas / Sensaciones',
                  border: OutlineInputBorder(),
                  alignLabelWithHint: true,
                ),
                maxLines: 3,
              ),
              const SizedBox(height: 24),

              ElevatedButton(
                onPressed: _submit,
                style: ElevatedButton.styleFrom(
                  backgroundColor: AppColors.primary,
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
                child: const Text('Guardar Registro', style: TextStyle(fontSize: 16, color: Colors.white)),
              ),
              const SizedBox(height: 24),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildNumberField(TextEditingController ctrl, String label) {
    return TextFormField(
      controller: ctrl,
      decoration: InputDecoration(
        labelText: label,
        border: const OutlineInputBorder(),
        contentPadding: const EdgeInsets.symmetric(horizontal: 8, vertical: 8),
      ),
      keyboardType: const TextInputType.numberWithOptions(decimal: true),
      style: const TextStyle(fontSize: 14),
    );
  }
}
