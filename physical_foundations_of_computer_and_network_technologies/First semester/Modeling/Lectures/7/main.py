import tkinter as tk
from tkinter import ttk
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg


# Функция для численного решения ОДУ методом Рунге-Кутты 4-го порядка
def projectile_motion(v0, angle, y0, k, dt=0.01, g=9.81):
    theta = np.radians(angle)
    vx, vy = v0 * np.cos(theta), v0 * np.sin(theta)
    x, y = 0, y0

    x_vals, y_vals, vx_vals, vy_vals, t_vals = [x], [y], [vx], [vy], [0]
    t = 0

    while y >= 0:
        v = np.sqrt(vx ** 2 + vy ** 2)
        fx = -k * vx
        fy = -k * vy - g

        # Метод Рунге-Кутты для vx и vy
        k1vx, k1vy = fx, fy
        k1x, k1y = vx, vy

        k2vx, k2vy = fx, fy
        k2x, k2y = vx + k1vx * dt / 2, vy + k1vy * dt / 2

        k3vx, k3vy = fx, fy
        k3x, k3y = vx + k2vx * dt / 2, vy + k2vy * dt / 2

        k4vx, k4vy = fx, fy
        k4x, k4y = vx + k3vx * dt, vy + k3vy * dt

        vx += (k1vx + 2 * k2vx + 2 * k3vx + k4vx) * dt / 6
        vy += (k1vy + 2 * k2vy + 2 * k3vy + k4vy) * dt / 6
        x += (k1x + 2 * k2x + 2 * k3x + k4x) * dt / 6
        y += (k1y + 2 * k2y + 2 * k3y + k4y) * dt / 6

        t += dt
        x_vals.append(x)
        y_vals.append(y)
        vx_vals.append(vx)
        vy_vals.append(vy)
        t_vals.append(t)

    return x_vals, y_vals, vx_vals, vy_vals, t_vals


def plot_graphs_in_tk(x_vals, y_vals, vx_vals, vy_vals, t_vals):
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(10, 5))

    # Траектория движения
    ax1.plot(x_vals, y_vals, label="Траектория")
    ax1.set_xlabel("Расстояние (м)")
    ax1.set_ylabel("Высота (м)")
    ax1.set_title("Траектория движения")
    ax1.legend()

    # Скорости по осям x и y
    ax2.plot(t_vals, vx_vals, label="Скорость по x")
    ax2.plot(t_vals, vy_vals, label="Скорость по y")
    ax2.set_xlabel("Время (с)")
    ax2.set_ylabel("Скорость (м/с)")
    ax2.set_title("Скорости от времени")
    ax2.legend()

    for widget in frame_plot.winfo_children():
        widget.destroy()

    canvas = FigureCanvasTkAgg(fig, master=frame_plot)
    canvas.draw()
    canvas.get_tk_widget().pack(fill=tk.BOTH, expand=True)


def run_simulation():
    v0 = float(entry_v0.get())
    angle = float(entry_angle.get())
    y0 = float(entry_y0.get())
    k = float(entry_k.get())

    x_vals, y_vals, vx_vals, vy_vals, t_vals = projectile_motion(v0, angle, y0, k)
    plot_graphs_in_tk(x_vals, y_vals, vx_vals, vy_vals, t_vals)


root = tk.Tk()
root.title("Моделирование движения тела с учетом сопротивления воздуха")

# Ввод
frame_input = ttk.Frame(root, padding="10")
frame_input.grid(row=0, column=0, sticky=(tk.W, tk.E, tk.N, tk.S))

ttk.Label(frame_input, text="Начальная скорость (м/с):").grid(column=0, row=0, sticky=tk.W)
entry_v0 = ttk.Entry(frame_input, width=10)
entry_v0.grid(column=1, row=0)

ttk.Label(frame_input, text="Угол броска (градусы):").grid(column=0, row=1, sticky=tk.W)
entry_angle = ttk.Entry(frame_input, width=10)
entry_angle.grid(column=1, row=1)

ttk.Label(frame_input, text="Начальная высота (м):").grid(column=0, row=2, sticky=tk.W)
entry_y0 = ttk.Entry(frame_input, width=10)
entry_y0.grid(column=1, row=2)

ttk.Label(frame_input, text="Коэффициент сопротивления k:").grid(column=0, row=3, sticky=tk.W)
entry_k = ttk.Entry(frame_input, width=10)
entry_k.grid(column=1, row=3)

# Кнопка запуска
ttk.Button(frame_input, text="Запустить", command=run_simulation).grid(column=0, row=4, columnspan=2)

frame_plot = ttk.Frame(root, padding="10")
frame_plot.grid(row=1, column=0, sticky=(tk.W, tk.E, tk.N, tk.S))

root.mainloop()
