import tkinter as tk
from tkinter import ttk
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import numpy as np


def calculate_energies(mass, k, b, t_max=10, dt=0.01):
    t = np.arange(0, t_max, dt)

    omega_0 = np.sqrt(k / mass)
    gamma = b / (2 * mass)
    omega_d = np.sqrt(omega_0**2 - gamma**2)

    x0 = 1.0
    v0 = 0.0

    x = x0 * np.exp(-gamma * t) * np.cos(omega_d * t)

    v = (
            -x0 * np.exp(-gamma * t)
            * (gamma * np.cos(omega_d * t) + omega_d * np.sin(omega_d * t))
    )

    kinetic_energy = 0.5 * mass * v**2
    potential_energy = 0.5 * k * x**2
    total_energy = kinetic_energy + potential_energy

    return t, kinetic_energy, potential_energy, total_energy


def update_plot():
    try:
        mass = float(mass_entry.get())
        k = float(k_entry.get())
        b = float(b_entry.get())

        t, kinetic_energy, potential_energy, total_energy = calculate_energies(
            mass, k, b
        )

        ax.clear()

        ax.plot(t, kinetic_energy, label="Кинетическая энергия",
                color="blue", linewidth=1.5)
        ax.plot(t, potential_energy, label="Потенциальная энергия",
                color="green", linewidth=1.5)
        ax.plot(t, total_energy, label="Полная механическая энергия",
                color="red", linestyle="--", linewidth=1.5)

        ax.grid(True)
        ax.set_xlabel("Время (с)")
        ax.set_ylabel("Энергия (Дж)")
        ax.set_title("Энергетические превращения")
        ax.legend()

        canvas.draw()

    except ValueError:
        result_label.config(
            text="Ошибка ввода, дробные данные вводить через точку"
        )


root = tk.Tk()
root.title("Энергетические превращения при колебаниях груза на пружине")
root.configure(bg="#086ca2")

mass_label = ttk.Label(root, text="Масса груза (кг):", background="#f0f0f0")
mass_label.grid(row=0, column=0, padx=5, pady=5)

mass_entry = ttk.Entry(root)
mass_entry.grid(row=0, column=1, padx=5, pady=5)

k_label = ttk.Label(root, text="Коэффициент жесткости (Н/м):", background="#f0f0f0")
k_label.grid(row=1, column=0, padx=5, pady=5)

k_entry = ttk.Entry(root)
k_entry.grid(row=1, column=1, padx=5, pady=5)

b_label = ttk.Label(root, text="Коэффициент сопротивления (кг/с):", background="#f0f0f0")
b_label.grid(row=2, column=0, padx=5, pady=5)

b_entry = ttk.Entry(root)
b_entry.grid(row=2, column=1, padx=5, pady=5)

update_button = ttk.Button(root, text="Построить график", command=update_plot)
update_button.grid(row=3, column=0, columnspan=2, pady=10)

result_label = ttk.Label(root, text="", background="#f0f0f0")
result_label.grid(row=4, column=0, columnspan=2)

fig, ax = plt.subplots(figsize=(6, 4))
canvas = FigureCanvasTkAgg(fig, master=root)
canvas.get_tk_widget().grid(row=5, column=0, columnspan=2)

root.mainloop()
