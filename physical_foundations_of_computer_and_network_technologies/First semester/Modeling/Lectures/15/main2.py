import tkinter as tk
from tkinter import Canvas

def calculate():
    try:
        voltage = float(entry_voltage.get())
        distance = float(entry_distance.get())
        dielectric_constant = float(entry_dielectric.get())
        area = float(entry_area.get())
        connected_to_source = var_connected.get()

        epsilon_0 = 8.854e-12

        # Ёмкость
        capacitance = (dielectric_constant * epsilon_0 * area) / distance

        # Напряженность электрического поля
        field_strength = voltage / distance

        # Заряд на пластинах
        charge = capacitance * voltage

        results = (
            f"Емкость: {capacitance:.2e} Ф\n"
            f"Напряженность электрического поля: {field_strength:.2e} В/м\n"
            f"Заряд на пластинах: {charge:.2e} Кл\n"
        )

        if connected_to_source == "Нет":
            new_dielectric_constant = float(entry_new_dielectric.get())

            # Перерасчет при отключении от источника питания
            new_capacitance = (new_dielectric_constant * epsilon_0 * area) / distance
            new_voltage = charge / new_capacitance
            new_field_strength = new_voltage / distance

            results += (
                f"\nРезультаты после изменения диэлектрика и отключения от источника:\n"
                f"Новая емкость: {new_capacitance:.2e} Ф\n"
                f"Новое напряжение: {new_voltage:.2e} В\n"
                f"Новая напряженность электрического поля: {new_field_strength:.2e} В/м"
            )

        label_results.config(text=results)

    except ValueError:
        label_results.config(text="Ошибка: Пожалуйста, введите корректные числовые значения.")

def toggle_new_dielectric():
    if var_connected.get() == "Нет":
        label_new_dielectric.grid(row=6, column=0, pady=5, sticky="e")
        entry_new_dielectric.grid(row=6, column=1, pady=5, sticky="w")
    else:
        label_new_dielectric.grid_remove()
        entry_new_dielectric.grid_remove()
        entry_new_dielectric.grid_remove()

root = tk.Tk()
root.title("Расчет параметров конденсатора")
root.configure(bg="#0773a1")

label_voltage = tk.Label(root, text="Напряжение (В):", bg="#f0f8ff")
label_voltage.grid(row=0, column=0, pady=5, sticky="e")
entry_voltage = tk.Entry(root)
entry_voltage.grid(row=0, column=1, pady=5, sticky="w")

label_distance = tk.Label(root, text="Расстояние между пластинами (м):", bg="#f0f8ff")
label_distance.grid(row=1, column=0, pady=5, sticky="e")
entry_distance = tk.Entry(root)
entry_distance.grid(row=1, column=1, pady=5, sticky="w")

label_dielectric = tk.Label(root, text="Диэлектрическая проницаемость (Ф/м):", bg="#f0f8ff")
label_dielectric.grid(row=2, column=0, pady=5, sticky="e")
entry_dielectric = tk.Entry(root)
entry_dielectric.grid(row=2, column=1, pady=5, sticky="w")

label_area = tk.Label(root, text="Площадь пластин (м^2):", bg="#f0f8ff")
label_area.grid(row=3, column=0, pady=5, sticky="e")
entry_area = tk.Entry(root)
entry_area.grid(row=3, column=1, pady=5, sticky="w")

label_connected = tk.Label(root, text="Подключен к источнику питания:", bg="#f0f8ff")
label_connected.grid(row=4, column=0, pady=5, sticky="e")
var_connected = tk.StringVar(value="Да")
option_connected = tk.OptionMenu(root, var_connected, "Да", "Нет", command=lambda _: toggle_new_dielectric())
option_connected.grid(row=4, column=1, pady=5, sticky="w")

label_new_dielectric = tk.Label(root, text="Новая диэлектрическая проницаемость (Ф/м):", bg="#f0f8ff")
entry_new_dielectric = tk.Entry(root)

button_calculate = tk.Button(root, text="Рассчитать", command=calculate, bg="#e6e6fa")
button_calculate.grid(row=7, column=0, columnspan=2, pady=10)

label_results = tk.Label(root, text="", justify="left", anchor="w", bg="#f0f8ff")
label_results.grid(row=8, column=0, columnspan=2, pady=10, sticky="w")

canvas = Canvas(root, width=400, height=200, bg="white")
canvas.grid(row=9, column=0, columnspan=2, pady=10)

canvas.create_rectangle(100, 50, 300, 70, fill="gray")
canvas.create_rectangle(100, 130, 300, 150, fill="gray")
canvas.create_text(200, 40, text="Пластина 1", font=("Arial", 10))
canvas.create_text(200, 160, text="Пластина 2", font=("Arial", 10))
canvas.create_line(200, 70, 200, 130, dash=(5, 2))
canvas.create_text(220, 100, text="d", font=("Arial", 10))

root.mainloop()
