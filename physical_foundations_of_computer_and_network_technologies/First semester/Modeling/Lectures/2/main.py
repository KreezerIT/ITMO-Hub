import tkinter as tk
from tkinter import ttk
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from matplotlib.animation import FuncAnimation


def calculate_trajectory(v0, angle, height):
    g = 9.81
    angle_rad = np.radians(angle)
    t_max_height = v0 * np.sin(angle_rad) / g
    t_total = (v0 * np.sin(angle_rad) + np.sqrt((v0 * np.sin(angle_rad)) ** 2 + 2 * g * height)) / g
    t = np.linspace(0, t_total, num=250)
    x = v0 * np.cos(angle_rad) * t
    y = height + v0 * np.sin(angle_rad) * t - 0.5 * g * t ** 2
    return t, x, y


def plot_trajectory(v0, angle, height):
    t, x, y = calculate_trajectory(v0, angle, height)
    fig, (ax1, ax2, ax3) = plt.subplots(1, 3, figsize=(15, 6))

    ax1.plot(x, y)
    ax1.set_title('Траектория движения')
    ax1.set_xlabel('X (метры)')
    ax1.set_ylabel('Y (метры)')
    ax1.grid(True)

    vx = v0 * np.cos(np.radians(angle))
    vy = v0 * np.sin(np.radians(angle)) - 9.81 * t
    speed = np.sqrt(vx ** 2 + vy ** 2)
    ax2.plot(t, speed)
    ax2.set_title('Зависимость скорости от времени')
    ax2.set_xlabel('Время (с)')
    ax2.set_ylabel('Скорость (м/с)')
    ax2.grid(True)

    ax3.plot(t, x, label='X (метры)')
    ax3.plot(t, y, label='Y (метры)')
    ax3.set_title('Зависимость координат от времени')
    ax3.set_xlabel('Время (с)')
    ax3.set_ylabel('Координаты (метры)')
    ax3.legend()
    ax3.grid(True)
    return fig, ax1, x, y



def animate_trajectory(v0, angle, height, ax, canvas):
    t, x, y = calculate_trajectory(v0, angle, height)
    line, = ax.plot([], [], color='red')

    def init():
        ax.set_xlim(0, max(x))
        ax.set_ylim(0, max(y) + height)
        return line,

    def update(frame):
        line.set_data(x[:frame], y[:frame])
        return line,

    ani = FuncAnimation(plt.gcf(), update, frames=len(t), init_func=init, blit=True, interval=20, repeat=False)
    canvas.draw()


def main():
    root = tk.Tk()
    root.title("Визуализация движения тела")

    screen_width = root.winfo_screenwidth()
    screen_height = root.winfo_screenheight()
    root.geometry(f"{screen_width}x{screen_height}")
    root.state('normal')


    canvas = tk.Canvas(root, width=150, height=100)
    canvas.grid(row=0, column=0, columnspan=2)

    canvas.create_rectangle(0, 0, 250, 30, fill='white', outline='white')
    canvas.create_rectangle(0, 30, 250, 60, fill='blue', outline='blue')
    canvas.create_rectangle(0, 60, 250, 90, fill='red', outline='red')

    tk.Label(root, text="Начальная скорость (м/с):").grid(row=1, column=0)
    entry_v0 = tk.Entry(root)
    entry_v0.grid(row=1, column=1)
    tk.Label(root, text="Угол броска (градусы):").grid(row=2, column=0)
    entry_angle = tk.Entry(root)
    entry_angle.grid(row=2, column=1)
    tk.Label(root, text="Начальная высота (м):").grid(row=3, column=0)
    entry_height = tk.Entry(root)
    entry_height.grid(row=3, column=1)


    def show_graphs():
        v0 = float(entry_v0.get())
        angle = float(entry_angle.get())
        height = float(entry_height.get())
        fig, ax1, x, y = plot_trajectory(v0, angle, height)
        canvas_fig = FigureCanvasTkAgg(fig, master=root)
        canvas_fig.draw()
        canvas_fig.get_tk_widget().grid(row=4, column=0, columnspan=2)

        animate_trajectory(v0, angle, height, ax1, canvas_fig)


    btn_show = ttk.Button(root, text="Показать графики / Начать анимацию", command=show_graphs)
    btn_show.grid(row=5, column=0, columnspan=2)
    root.mainloop()


if __name__ == "__main__":
    main()
