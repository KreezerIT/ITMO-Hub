import tkinter as tk
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.animation import FuncAnimation
FPS = 60

class WheelAnimationApp:
    def __init__(self, window):

        self.window = window
        self.window.title("Анимация движения колеса")
        self.window.geometry("1600x900")
        self.window.configure(bg='gray')
        self.window.protocol("WM_DELETE_WINDOW", self.close_app)


        text_font = ("Arial", 14)
        text_color = 'blue'


        self.graph_frame = tk.Frame(window)
        self.graph_frame.pack(side=tk.TOP, fill=tk.BOTH, expand=True)


        self.figure, self.graph = plt.subplots()
        self.graph.set_aspect('equal')
        self.graph.grid(True, which='both', color='gray', linestyle='--')
        self.graph.minorticks_on()  # Minor ticks
        self.graph.grid(True, which='minor', color='lightgray', linestyle=':', linewidth=0.5)
        self.figure.patch.set_facecolor('gray')
        self.graph.set_facecolor('black')
        self.graph.tick_params(colors='white')

        self.canvas = FigureCanvasTkAgg(self.figure, master=self.graph_frame)
        self.canvas.get_tk_widget().pack(fill=tk.BOTH, expand=1)


        self.control_frame = tk.Frame(window, bg='gray')
        self.control_frame.pack(side=tk.BOTTOM, fill=tk.X, pady = 60)


        self.lbl_radius = tk.Label(self.control_frame, text="Введите радиус колеса:", font=text_font, bg='gray', fg=text_color)
        self.lbl_radius.grid(row=0, column=0, padx=10, pady=10)
        self.input_radius = tk.Entry(self.control_frame, font=text_font)
        self.input_radius.grid(row=0, column=1, padx=10, pady=10)

        self.lbl_speed = tk.Label(self.control_frame, text="Введите скорость центра масс:", font=text_font, bg='gray', fg=text_color)
        self.lbl_speed.grid(row=1, column=0, padx=10, pady=8)
        self.input_speed = tk.Entry(self.control_frame, font=text_font)
        self.input_speed.grid(row=1, column=1, padx=10, pady=10)

        self.btn_start = tk.Button(self.control_frame, text="Старт анимации", font=text_font, command=self.begin_animation, padx=8, pady=4)
        self.btn_start.grid(row=2, column=0, padx=10, pady=10)

        self.btn_pause = tk.Button(self.control_frame, text="Пауза анимации", font=text_font, command=self.toggle_pause, padx=8, pady=4)
        self.btn_pause.grid(row=2, column=1, padx=10, pady=10)


        self.animation = None
        self.running = False
        self.paused = False

        self.create_tricolor()

    def close_app(self):
        self.window.quit()
        self.window.destroy()

    def begin_animation(self):
        try:
            self.radius = float(self.input_radius.get())
            self.speed = float(self.input_speed.get())
        except ValueError:
            print("Ошибка: введите корректные числа!")
            return

        if self.animation:
            self.animation.event_source.stop()


        self.graph.clear()
        self.graph.set_aspect('equal')
        self.graph.grid(True, which='both', color='gray', linestyle='--')
        self.graph.minorticks_on()  # Minor ticks
        self.graph.grid(True, which='minor', color='lightgray', linestyle=':', linewidth=0.5)
        self.graph.set_xlim(0, 10 * self.radius)
        self.graph.set_ylim(-3 * self.radius, 3 * self.radius)
        self.graph.set_facecolor('black')
        self.graph.tick_params(colors='white')

        self.time_elapsed = 0
        self.center_point, = self.graph.plot([], [], 'ro')
        self.wheel_outline, = self.graph.plot([], [], 'w-', lw=2)
        self.trace_line, = self.graph.plot([], [], 'r-', lw=1)
        self.radius_vector, = self.graph.plot([], [], 'b-', lw=2)

        self.running = True
        self.paused = False
        self.btn_pause.config(text="Пауза анимации")
        self.animation = FuncAnimation(self.figure, self.refresh_frame, frames=np.linspace(0, 2 * np.pi, FPS), interval=1000 // FPS, blit=True)

    def toggle_pause(self):
        """ Переключение паузы и возобновления анимации """
        if not self.running:
            return

        if self.paused:
            self.animation.event_source.start()
            self.btn_pause.config(text="Пауза анимации")
        else:
            self.animation.event_source.stop()
            self.btn_pause.config(text="Продолжить анимацию")

        self.paused = not self.paused

    def calculate_positions(self, time):
        """ Расчёт положения центра и точки обода на колесе """
        x_center = self.speed * time
        y_center = 0
        angle = (self.speed * time) / self.radius
        x_rim = x_center + self.radius * np.sin(angle)
        y_rim = self.radius * np.cos(angle)
        return x_center, y_center, x_rim, y_rim

    def refresh_frame(self, t):
        """ Обновление кадров для анимации """
        if not self.running or self.paused:
            return self.center_point, self.wheel_outline, self.trace_line, self.radius_vector

        self.time_elapsed += 1 / FPS
        x_center, y_center, x_rim, y_rim = self.calculate_positions(self.time_elapsed)

        self.center_point.set_data([x_rim], [y_rim])

        theta_values = np.linspace(0, 2 * np.pi, 100)
        x_wheel = x_center + self.radius * np.cos(theta_values)
        y_wheel = y_center + self.radius * np.sin(theta_values)

        self.wheel_outline.set_data(x_wheel, y_wheel)

        self.trace_line.set_data(np.append(self.trace_line.get_xdata(), x_rim), np.append(self.trace_line.get_ydata(), y_rim))

        self.radius_vector.set_data([x_center, x_rim], [y_center, y_rim])

        if x_rim > self.graph.get_xlim()[1] - 2:
            self.graph.set_xlim(self.graph.get_xlim()[0], x_rim + 2)
            self.canvas.draw()

        return self.center_point, self.wheel_outline, self.trace_line, self.radius_vector

    def create_tricolor(self):

        self.white_frame = tk.Frame(self.control_frame, bg='white', width=300, height=65)
        self.white_frame.grid(row=0, column=2, padx=90)

        self.blue_frame = tk.Frame(self.control_frame, bg='blue', width=300, height=65)
        self.blue_frame.grid(row=1, column=2, padx=90)

        self.red_frame = tk.Frame(self.control_frame, bg='red', width=300, height=65)
        self.red_frame.grid(row=2, column=2, padx=90)

if __name__ == "__main__":
    window = tk.Tk()
    app = WheelAnimationApp(window)
    window.mainloop()
