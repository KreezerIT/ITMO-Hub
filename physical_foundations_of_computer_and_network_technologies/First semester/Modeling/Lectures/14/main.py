import tkinter as tk
from tkinter import ttk
import math
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

def calculate_and_plot():
    try:
        epsilon1 = float(epsilon1_entry.get())
        epsilon2 = float(epsilon2_entry.get())
        field_angle = float(angle_entry.get())
        field_magnitude = float(magnitude_entry.get())

        field_angle_rad = math.radians(field_angle)

        refracted_angle_rad = math.asin((epsilon1 / epsilon2) * math.sin(field_angle_rad))

        E1_x, E1_y = field_magnitude * math.cos(field_angle_rad), field_magnitude * math.sin(field_angle_rad)
        E2_x, E2_y = field_magnitude * epsilon1 / epsilon2 * math.cos(refracted_angle_rad), -field_magnitude * epsilon1 / epsilon2 * math.sin(refracted_angle_rad)

        fig, ax = plt.subplots(figsize=(6, 6))

        ax.axhline(0, color='black', linewidth=0.8, linestyle='--')

        ax.set_xlabel('Ось X', fontsize=12)
        ax.set_ylabel('Ось Y', fontsize=12)

        ax.arrow(0, 0, E1_x, E1_y, head_width=0.1, color='blue', label='E (среда 1)')
        ax.arrow(0, 0, E2_x, E2_y, head_width=0.1, color='green', label='E (среда 2)')

        refracted_angle_deg = math.degrees(refracted_angle_rad)
        fig.text(0.5, 0.95, f'Угол преломления: {refracted_angle_deg:.2f}°', color='red', fontsize=12,
                 ha='center', va='center', bbox=dict(facecolor='white', alpha=0.8))

        # Электрическое смещение
        D1_x, D1_y = epsilon1 * E1_x, epsilon1 * E1_y
        D2_x, D2_y = epsilon2 * E2_x, epsilon2 * E2_y
        ax.arrow(0, 0, D1_x, D1_y, head_width=0.1, color='cyan', label='D (среда 1)', linestyle='dotted')
        ax.arrow(0, 0, D2_x, D2_y, head_width=0.1, color='orange', label='D (среда 2)', linestyle='dotted')

        ax.set_xlim(-1.5 * field_magnitude, 1.5 * field_magnitude)
        ax.set_ylim(-1.5 * field_magnitude, 1.5 * field_magnitude)
        ax.set_aspect('equal', adjustable='box')
        ax.legend()
        ax.grid(True)

        for widget in plot_frame.winfo_children():
            widget.destroy()

        canvas = FigureCanvasTkAgg(fig, master=plot_frame)
        canvas.draw()
        canvas.get_tk_widget().pack()

    except Exception as e:
        error_label.config(text=f"Ошибка: {str(e)}")

root = tk.Tk()
root.title("Визуализация граничных условий")

input_frame = ttk.Frame(root, padding=10)
input_frame.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)

plot_frame = ttk.Frame(root, padding=10)
plot_frame.pack(side=tk.RIGHT, fill=tk.BOTH, expand=True)

ttk.Label(input_frame, text="Диэлектрическая проницаемость среды 1:").pack(anchor=tk.W)
epsilon1_entry = ttk.Entry(input_frame)
epsilon1_entry.pack(fill=tk.X)
epsilon1_entry.insert(0, "1.0")

ttk.Label(input_frame, text="Диэлектрическая проницаемость среды 2:").pack(anchor=tk.W)
epsilon2_entry = ttk.Entry(input_frame)
epsilon2_entry.pack(fill=tk.X)
epsilon2_entry.insert(0, "2.0")

ttk.Label(input_frame, text="Угол напряженности (в градусах):").pack(anchor=tk.W)
angle_entry = ttk.Entry(input_frame)
angle_entry.pack(fill=tk.X)
angle_entry.insert(0, "45")

ttk.Label(input_frame, text="Модуль напряженности поля:").pack(anchor=tk.W)
magnitude_entry = ttk.Entry(input_frame)
magnitude_entry.pack(fill=tk.X)
magnitude_entry.insert(0, "1.0")

calculate_button = ttk.Button(input_frame, text="Рассчитать и визуализировать", command=calculate_and_plot)
calculate_button.pack(pady=10)

error_label = ttk.Label(input_frame, text="", foreground="red")
error_label.pack()

root.mainloop()
