import numpy as np
import matplotlib
matplotlib.use("TkAgg")
import matplotlib.pyplot as plt

from tkinter import Tk, Button, Scale, HORIZONTAL


charges = [
    {"pos": [-1, 0], "q": 1},
    {"pos": [1, 0], "q": -1},
]

x = np.linspace(-3, 3, 200)
y = np.linspace(-3, 3, 200)
X, Y = np.meshgrid(x, y)


def potential(x, y, charges):
    V = np.zeros_like(x)
    for charge in charges:
        r = np.sqrt(
            (x - charge["pos"][0]) ** 2 +
            (y - charge["pos"][1]) ** 2
        )
        r[r == 0] = 1e-6
        V += charge["q"] / r
    return V


def update_plot():
    V = potential(X, Y, charges)

    plt.clf()
    plt.contour(X, Y, V, levels=20, cmap="RdBu")
    plt.colorbar(label="Потенциал")

    plt.scatter(
        [c["pos"][0] for c in charges],
        [c["pos"][1] for c in charges],
        color="black",
        s=50,
        label="Заряды",
    )

    plt.title("Эквипотенциальные поверхности системы точечных зарядов")
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


root = Tk()
root.title("Настройки зарядов")

scale_charge1_x = Scale(
    root, from_=-3, to=3, orient=HORIZONTAL,
    label="Положение заряда 1 (x)", length=150,
    command=move_charge1_x
)
scale_charge1_x.set(charges[0]["pos"][0])
scale_charge1_x.pack()

scale_charge1_y = Scale(
    root, from_=-3, to=3, orient=HORIZONTAL,
    label="Положение заряда 1 (y)", length=150,
    command=move_charge1_y
)
scale_charge1_y.set(charges[0]["pos"][1])
scale_charge1_y.pack()

scale_charge2_x = Scale(
    root, from_=-3, to=3, orient=HORIZONTAL,
    label="Положение заряда 2 (x)", length=150,
    command=move_charge2_x
)
scale_charge2_x.set(charges[1]["pos"][0])
scale_charge2_x.pack()

scale_charge2_y = Scale(
    root, from_=-3, to=3, orient=HORIZONTAL,
    label="Положение заряда 2 (y)", length=150,
    command=move_charge2_y
)
scale_charge2_y.set(charges[1]["pos"][1])
scale_charge2_y.pack()

scale_charge1_q = Scale(
    root, from_=-10, to=10, orient=HORIZONTAL,
    label="Величина заряда 1 (Кл)", length=150,
    command=change_charge1_q
)
scale_charge1_q.set(charges[0]["q"])
scale_charge1_q.pack()

scale_charge2_q = Scale(
    root, from_=-10, to=10, orient=HORIZONTAL,
    label="Величина заряда 2 (Кл)", length=150,
    command=change_charge2_q
)
scale_charge2_q.set(charges[1]["q"])
scale_charge2_q.pack()

Button(root, text="Обновить график", command=update_plot).pack()

plt.figure(figsize=(8, 6))
update_plot()
plt.show(block=False)

root.mainloop()
