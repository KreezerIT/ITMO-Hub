import numpy as np
import matplotlib.pyplot as plt
import matplotlib
matplotlib.use('TkAgg')

# Параметры
wavelength = 500e-9      # длина волны (м), например 500 нм
L = 1.0                  # расстояние до экрана наблюдения (м)
N = 1024                 # разрешение (размер матрицы)
dx = 10e-6               # шаг дискретизации объекта (м)
k = 2 * np.pi / wavelength  # волновое число

# Создание координатной сетки
x = np.linspace(-N//2, N//2 - 1, N) * dx
y = np.linspace(-N//2, N//2 - 1, N) * dx
X, Y = np.meshgrid(x, y)

# Задание амплитудного распределения (пример — круглая апертура)
R = 100e-6  # радиус апертуры (м)
aperture = np.where(X**2 + Y**2 <= R**2, 1.0, 0.0)

# Вычисление 2D Фурье-преобразования
fft_result = np.fft.fftshift(np.fft.fft2(aperture))
intensity = np.abs(fft_result)**2

# Масштабирование пространственной частоты к физическим координатам
fx = np.fft.fftshift(np.fft.fftfreq(N, d=dx))
fy = np.fft.fftshift(np.fft.fftfreq(N, d=dx))
FX, FY = np.meshgrid(fx, fy)

# Переводим частоты в координаты в дальней зоне: x' = λ L fx
x_far = wavelength * L * FX
y_far = wavelength * L * FY

# Отображение
plt.figure(figsize=(8, 6))
plt.imshow(intensity / intensity.max(), extent=[x_far.min()*1e3, x_far.max()*1e3, y_far.min()*1e3, y_far.max()*1e3],
           cmap='inferno')
plt.title("Дифракционная картина (Фраунгофер)")
plt.xlabel("x (мм)")
plt.ylabel("y (мм)")
plt.colorbar
plt.show()