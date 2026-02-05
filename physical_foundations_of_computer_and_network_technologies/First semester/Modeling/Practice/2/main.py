import numpy as np
import matplotlib.pyplot as plt

e = 1.6e-19  # заряд электрона, Кл
me = 9.11e-31  # масса электрона, кг
r_inner = 9e-2  # внутренний радиус, м
r_outer = 19e-2  # внешний радиус, м
L = 27e-2  # длина конденсатора, м
Vx = 9.5e5  # начальная скорость вдоль x, м/с
d = r_outer - r_inner  # расстояние между обкладками

U_min = (me * Vx**2 * d) / (2 * e * r_inner)
t_flight = L / Vx

a_y = e * U_min / (me * d)
Vy_final = a_y * t_flight
V_final = np.sqrt(Vx**2 + Vy_final**2)
time = np.linspace(0, t_flight, 1000)

y_t = 0.5 * a_y * time**2
Vy_t = a_y * time
ay_t = np.full_like(time, a_y)
x_t = Vx * time

plt.figure(figsize=(10, 8))

# График y(x)
plt.subplot(2, 2, 1)
plt.plot(x_t, y_t, label="y(x)")
plt.xlabel("x, м")
plt.ylabel("y, м")
plt.title("Траектория y(x)")
plt.grid()
plt.legend()

# График Vy(t)
plt.subplot(2, 2, 2)
plt.plot(time, Vy_t, label="Vy(t)", color="orange")
plt.xlabel("t, с")
plt.ylabel("Vy, м/с")
plt.title("Скорость Vy(t)")
plt.grid()
plt.legend()

# График ay(t)
plt.subplot(2, 2, 3)
plt.plot(time, ay_t, label="ay(t)", color="green")
plt.xlabel("t, с")
plt.ylabel("ay, м/с²")
plt.title("Ускорение ay(t)")
plt.grid()
plt.legend()

# График y(t)
plt.subplot(2, 2, 4)
plt.plot(time, y_t, label="y(t)", color="red")
plt.xlabel("t, с")
plt.ylabel("y, м")
plt.title("Координата y(t)")
plt.grid()
plt.legend()

plt.tight_layout()
plt.show()

print(f"Минимальное напряжение: {U_min:.2f} В")
print(f"Время полета: {t_flight:.2e} с")
print(f"Конечная скорость Vy: {Vy_final:.2e} м/с")
print(f"Полная конечная скорость: {V_final:.2e} м/с")
