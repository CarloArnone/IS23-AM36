
# Functioning of getPointsForAdiancent() method

### <span style="color:FloralWhite">*Starting Matrix*</span>

|     | 0                                   | 1                                   | 2                                   | 3                                   | 4                                  |
|-----|-------------------------------------|-------------------------------------|-------------------------------------|-------------------------------------|------------------------------------|
| 0   | <span style="color:purple">▉</span> | <span style="color:purple">▉</span> | <span style="color:purple">▉</span> | <span style="color:orange">▉</span> | <span style="color:white">▉</span> |
| 1   | <span style="color:purple">▉</span> | <span style="color:white">▉</span>  | <span style="color:green">▉</span>  | <span style="color:orange">▉</span> | <span style="color:white">▉</span> |
| 2   | <span style="color:purple">▉</span> | <span style="color:cyan">▉</span>   | <span style="color:green">▉</span>  | <span style="color:orange">▉</span> | <span style="color:white">▉</span> |
| 3   | <span style="color:cyan">▉</span>   | <span style="color:cyan">▉</span>   | <span style="color:green">▉</span>  | <span style="color:orange">▉</span> | <span style="color:green">▉</span> |
| 4   | <span style="color:cyan">▉</span>   | <span style="color:cyan">▉</span>   | <span style="color:green">▉</span>  | <span style="color:green">▉</span>  | <span style="color:green">▉</span> |
| 5   | <span style="color:blue">▉</span>   | <span style="color:blue">▉</span>   | <span style="color:blue">▉</span>   | <span style="color:white">▉</span>  | <span style="color:white">▉</span> |

### 1° Iteration -- see purple and check them all
|   | 0 | 1 | 2 | 3 | 4 |
| ----------- | ----------- | ----------- | ----------- | ----------- |  ----------- |
| 0 | ✔ | ✔ | ✔ |  |  |
| 1 | ✔ |  |  |  |  |
| 2 | ✔ |  |  |  |  |
| 3 |  |  |  |  |  |
| 4 |  |  |  |  |  |
| 5 |  |  |  |  |  |

### 2° Iteration -- see orange and check them all
|   | 0 | 1 | 2 | 3 | 4 |
| ----------- | ----------- | ----------- | ----------- | ----------- |  ----------- |
| 0 | ✔ | ✔ | ✔ | ✔ |  |
| 1 | ✔ |  |  | ✔ |  |
| 2 | ✔ |  |  | ✔ |  |
| 3 |  |  |  | ✔ |  |
| 4 |  |  |  |  |  |
| 5 |  |  |  |  |  |
### 3° Iteration -- see white and check them all
|   | 0 | 1 | 2 | 3 | 4 |
| ----------- | ----------- | ----------- | ----------- | ----------- |  ----------- |
| 0 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 1 | ✔ |  |  | ✔ | ✔ |
| 2 | ✔ |  |  | ✔ | ✔ |
| 3 |  |  |  | ✔ |  |
| 4 |  |  |  |  |  |
| 5 |  |  |  |  |  |
### 4° Iteration -- see again purple but is already checked so dows not recount the group.
|   | 0 | 1 | 2 | 3 | 4 |
| ----------- | ----------- | ----------- | ----------- | ----------- |  ----------- |
| 0 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 1 | ✔ |  |  | ✔ | ✔ |
| 2 | ✔ |  |  | ✔ | ✔ |
| 3 |  |  |  | ✔ |  |
| 4 |  |  |  |  |  |
| 5 |  |  |  |  |  |

### Skip of white check beacuse is only one -- trivial matter

### 5° Iteration -- see green and check them all
|   | 0 | 1 | 2 | 3 | 4 |
| ----------- | ----------- | ----------- | ----------- | ----------- |  ----------- |
| 0 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 1 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 2 | ✔ |  | ✔ | ✔ | ✔ |
| 3 |  |  | ✔ | ✔ | ✔ |
| 4 |  |  | ✔ | ✔ | ✔ |
| 5 |  |  |  |  |  |

### Does the same with orange and white as 4° iteration.


### 6° Iteration -- see cyan and check them all
|   | 0 | 1 | 2 | 3 | 4 |
| ----------- | ----------- | ----------- | ----------- | ----------- |  ----------- |
| 0 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 1 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 2 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 3 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 4 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 5 |  |  |  |  |  |


### Skip everything until reach last row.

### 7° Iteration -- see blue and check them all
|   | 0 | 1 | 2 | 3 | 4 |
| ----------- | ----------- | ----------- | ----------- | ----------- |  ----------- |
| 0 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 1 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 2 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 3 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 4 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 5 | ✔ | ✔ | ✔ |  |  |

### 8° Iteration -- see white and check them all
|   | 0 | 1 | 2 | 3 | 4 |
| ----------- | ----------- | ----------- | ----------- | ----------- |  ----------- |
| 0 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 1 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 2 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 3 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 4 | ✔ | ✔ | ✔ | ✔ | ✔ |
| 5 | ✔ | ✔ | ✔ | ✔ | ✔ |