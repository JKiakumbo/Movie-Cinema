package pl.jkiakumbo.cinema.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jkiakumbo.cinema.domain.discount.Discount;
import pl.jkiakumbo.cinema.domain.discount.DiscountType;
import pl.jkiakumbo.cinema.service.crud.DiscountService;

import java.util.Random;

@Aspect
@Component
public class LuckyWinnerAspect {

    private DiscountService discountService;
    private Random random;

    @Autowired
    public LuckyWinnerAspect(DiscountService discountService) {
        this.discountService = discountService;
        this.random = new Random();
    }

    @Pointcut("execution( * pl.jkiakumbo.cinema.service.crud.DiscountService.getDiscount(*) )")
    public void getDiscountMethodPointcut() {}

    @Around(value = "getDiscountMethodPointcut()")
    public Object getDiscountMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        Discount originalDiscount = (Discount) joinPoint.proceed();

        if (isLuckyWinner()) {
            return discountService.findByType(DiscountType.LUCKY_WINNER);
        }

        return originalDiscount;
    }

    private boolean isLuckyWinner() {
        return random.nextInt(100) == 77;
    }
}
