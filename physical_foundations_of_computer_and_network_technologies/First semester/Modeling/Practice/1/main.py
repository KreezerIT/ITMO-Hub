import numpy as np
import matplotlib.pyplot as plt

# Параметры
m = 2.0 # сокращается
R = 5.0
a = np.pi/2 + np.pi/6
g = 9.81
k_fr = 0.02

# Минимальная начальная скорость для прохождения всей дуги
E_pot = g * R * (1 - np.cos(a))
F_fr = k_fr * g * R * a
v0 = np.sqrt(2 * E_pot + 2 * F_fr)

# Визуализация дуги
theta_values = np.linspace(0, a, 1000)
Arc_x = R * np.sin(theta_values)
Arc_y = -R * np.cos(theta_values) + R

# Визуализазция тракетории после отрыва от дуги
vx_AfterSeparation = v0 * np.cos(a)
vy_AfterSeparation = v0 * np.sin(a)

FlyTime_values = np.linspace(0, 3, 1000)
Flying_x = Arc_x[-1] + vx_AfterSeparation * FlyTime_values
Flying_y = Arc_y[-1] + vy_AfterSeparation * FlyTime_values - 0.5 * g * FlyTime_values**2

# График
plt.figure(figsize=(10, 10))
plt.plot(Arc_x, Arc_y, label="Дуга", color="purple")
plt.plot(Flying_x, Flying_y, label="Траектория после отрыва", color="red", linestyle="-.")

# Оформление графика
plt.xlabel("x, м")
plt.ylabel("y, м")
plt.axhline(0, color="black", linewidth=0.5)
plt.axvline(0, color="black", linewidth=0.5)
plt.legend()
plt.title("Траектория движения тела по мёртвой петле")
plt.grid()
plt.show()