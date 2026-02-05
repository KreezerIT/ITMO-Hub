import numpy as np
import matplotlib

matplotlib.use('TkAgg')
import matplotlib.pyplot as plt
from tkinter import Tk, Button, Scale, HORIZONTAL

charges = [
    {"pos": [-1, 0], "q": 1},
    {"pos": [1, 0], "q": -1},
]

# Параметры диполя
dipole_pos = [0, 2]  # Начальная позиция диполя
dipole_moment = 1  # Модуль дипольного момента
dipole_angle = 0  # Угол дипольного момента

x = np.linspace(-3, 3, 200)
y = np.linspace(-3, 3, 200)
X, Y = np.meshgrid(x, y)


def potential(x, y, charges):
    V = np.zeros_like(x)
    for charge in charges:
        r = np.sqrt((x - charge["pos"][0]) ** 2 + (y - charge["pos"][1]) ** 2)
        r[r == 0] = 1e-6
        V += charge["q"] / r
    return V


def electric_field(x, y, charges):
    Ex = np.zeros_like(x, dtype=np.float64)
    Ey = np.zeros_like(y, dtype=np.float64)

    # Каждый заряд
    for charge in charges:
        dx = x - charge["pos"][0]
        dy = y - charge["pos"][1]
        r = np.sqrt(dx ** 2 + dy ** 2)

        r = np.where(r == 0, 1e-6, r)

        # Напряженность электрического поля
        Ex += charge["q"] * dx / r ** 3
        Ey += charge["q"] * dy / r ** 3

    return Ex, Ey


def calculate_torque(dipole_pos, dipole_moment, dipole_angle, Ex, Ey):
    # Направление дипольного момента
    p_x = dipole_moment * np.cos(dipole_angle)
    p_y = dipole_moment * np.sin(dipole_angle)

    # Момент силы
    M_z = p_x * Ey - p_y * Ex

    return M_z


def update_plot():
    V = potential(X, Y, charges)
    plt.clf()
    plt.contour(X, Y, V, levels=20, cmap="RdBu")
    plt.colorbar(label="Потенциал")
    plt.scatter(*zip(*[charge["pos"] for charge in charges]), color="black", s=50, label="Заряды")

    # Диполь
    dipole_end_x = dipole_pos[0] + dipole_moment * np.cos(dipole_angle)
    dipole_end_y = dipole_pos[1] + dipole_moment * np.sin(dipole_angle)
    plt.plot([dipole_pos[0], dipole_end_x], [dipole_pos[1], dipole_end_y], 'r-', label="Диполь")

    # Напряженность в точке диполя
    Ex, Ey = electric_field(dipole_pos[0], dipole_pos[1], charges)
    force_x = dipole_moment * (np.cos(dipole_angle) * Ex + np.sin(dipole_angle) * Ey)
    force_y = dipole_moment * (-np.sin(dipole_angle) * Ex + np.cos(dipole_angle) * Ey)
    force_magnitude = np.sqrt(force_x ** 2 + force_y ** 2)

    # Момент силы
    M_z = calculate_torque(dipole_pos, dipole_moment, dipole_angle, Ex, Ey)

    plt.quiver(dipole_pos[0], dipole_pos[1], force_x, force_y, angles='xy', scale_units='xy', scale=1, color="blue",
               label="Сила на диполь")
    plt.quiver(dipole_pos[0], dipole_pos[1], 0, M_z, angles='xy', scale_units='xy', scale=1, color="green",
               label="Момент силы на диполь")

    plt.title(f"Эквипотенциальные поверхности и диполь\nМодуль сил: {force_magnitude:.2e} Н, Момент силы: {M_z:.2e} Н·м")
    plt.xlabel("x")
    plt.ylabel("y")
    plt.legend()
    plt.grid()
    plt.draw()


def move_charge1_x(val):
    charges[0]["pos"][0] = float(val)
    update_plot()


def move_charge1_y(val):
    charges[0]["pos"][1] = float(val)
    update_plot()


def move_charge2_x(val):
    charges[1]["pos"][0] = float(val)
    update_plot()


def move_charge2_y(val):
    charges[1]["pos"][1] = float(val)
    update_plot()


def change_charge1_q(val):
    charges[0]["q"] = float(val)
    update_plot()


def change_charge2_q(val):
    charges[1]["q"] = float(val)
    update_plot()


def move_dipole_x(val):
    global dipole_pos
    dipole_pos[0] = float(val)
    update_plot()


def move_dipole_y(val):
    global dipole_pos
    dipole_pos[1] = float(val)
    update_plot()


def change_dipole_moment(val):
    global dipole_moment
    dipole_moment = float(val)
    update_plot()


def change_dipole_angle(val):
    global dipole_angle
    dipole_angle = np.radians(float(val))
    update_plot()


root = Tk()
root.title("Настройки зарядов и диполя")

# Ползунки для зарядов
scale_charge1_x = Scale(root, from_=-3, to=3, orient=HORIZONTAL, label="Положение заряда 1 (x)", length=205)
scale_charge1_x.set(charges[0]["pos"][0])
scale_charge1_x.pack()
scale_charge1_x.bind("<Motion>", lambda event: move_charge1_x(scale_charge1_x.get()))

scale_charge1_y = Scale(root, from_=-3, to=3, orient=HORIZONTAL, label="Положение заряда 1 (y)", length=205)
scale_charge1_y.set(charges[0]["pos"][1])
scale_charge1_y.pack()
scale_charge1_y.bind("<Motion>", lambda event: move_charge1_y(scale_charge1_y.get()))

scale_charge2_x = Scale(root, from_=-3, to=3, orient=HORIZONTAL, label="Положение заряда 2 (x)", length=205)
scale_charge2_x.set(charges[1]["pos"][0])
scale_charge2_x.pack()
scale_charge2_x.bind("<Motion>", lambda event: move_charge2_x(scale_charge2_x.get()))

scale_charge2_y = Scale(root, from_=-3, to=3, orient=HORIZONTAL, label="Положение заряда 2 (y)", length=205)
scale_charge2_y.set(charges[1]["pos"][1])
scale_charge2_y.pack()
scale_charge2_y.bind("<Motion>", lambda event: move_charge2_y(scale_charge2_y.get()))

scale_charge1_q = Scale(root, from_=-10, to=10, orient=HORIZONTAL, label="Величина заряда 1 (Кл)", length=205)
scale_charge1_q.set(charges[0]["q"])
scale_charge1_q.pack()
scale_charge1_q.bind("<Motion>", lambda event: change_charge1_q(scale_charge1_q.get()))

scale_charge2_q = Scale(root, from_=-10, to=10, orient=HORIZONTAL, label="Величина заряда 2 (Кл)", length=205)
scale_charge2_q.set(charges[1]["q"])
scale_charge2_q.pack()
scale_charge2_q.bind("<Motion>", lambda event: change_charge2_q(scale_charge2_q.get()))

# Ползунки для диполя
scale_dipole_x = Scale(root, from_=-3, to=3, orient=HORIZONTAL, label="Положение диполя (x)", length=205)
scale_dipole_x.set(dipole_pos[0])
scale_dipole_x.pack()
scale_dipole_x.bind("<Motion>", lambda event: move_dipole_x(scale_dipole_x.get()))

scale_dipole_y = Scale(root, from_=-3, to=3, orient=HORIZONTAL, label="Положение диполя (y)", length=205)
scale_dipole_y.set(dipole_pos[1])
scale_dipole_y.pack()
scale_dipole_y.bind("<Motion>", lambda event: move_dipole_y(scale_dipole_y.get()))

scale_dipole_moment = Scale(root, from_=0, to=10, orient=HORIZONTAL, label="Модуль дипольного момента (Кл*м)", length=205)
scale_dipole_moment.set(dipole_moment)
scale_dipole_moment.pack()
scale_dipole_moment.bind("<Motion>", lambda event: change_dipole_moment(scale_dipole_moment.get()))

scale_dipole_angle = Scale(root, from_=0, to=360, orient=HORIZONTAL, label="Угол дипольного момента (Гр)", length=205)
scale_dipole_angle.set(np.degrees(dipole_angle))
scale_dipole_angle.pack()
scale_dipole_angle.bind("<Motion>", lambda event: change_dipole_angle(scale_dipole_angle.get()))

update_button = Button(root, text="Обновить график", command=update_plot)
update_button.pack()

plt.figure(figsize=(8, 6))
update_plot()

plt.show(block=False)
root.mainloop()
